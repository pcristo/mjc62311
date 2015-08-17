package replication;

import common.UDP;
import common.logger.LoggerClient;
import common.util.Config;
import replication.messageObjects.*;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ReplicaManager {


    // List tracking all active Replica Managers
    public HashMap<Long, Integer> rmList = new HashMap<>();
    // Port current replica manager is using
    public Integer rmPort = null;
    // Tracking the last time heartbeat we recieved
    private ConcurrentHashMap<Integer, Long> heartbeatHistory = new ConcurrentHashMap<>();

    // Heartbeat thread
    Thread hb = null;


    public static void main(String[] args) {
        new ReplicaManager().start();
    }

    /**
     * Start the replica manager
     */
    public void start() {


        Replica replica = new Replica();
        boolean replicaStarted = false;
        // We start at port 9998, but if it's busy, we pick another one
        int port = 9998;
        // Function to occur when a message is received
        Consumer<MessageEnvelope> callback = (MessageEnvelope me) -> {

            if(me.getType() == MessageType.OrderMessage) {
                // Handle actual message, send to replica
                LoggerClient.log("Order Message received");
                replica.ToReceive(me);
            } else if(me.getType() == MessageType.PortNessage) {
                // Update list of known RM ports
                LoggerClient.log("Received port message");
                rmList = me.getPortMessage().getPorts();
                LoggerClient.log("Tracked RM's: " + rmList.toString());
            } else if(me.getType() == MessageType.HeartbeatMessage){
                // Heartbeat recieved
                LoggerClient.log("Heartbeat received from RM at port: " + me.getHeartbeatMessage().getPort().toString());
                    heartbeatHistory.put(me.getHeartbeatMessage().getPort(), System.currentTimeMillis());
                    // Check if anyone in history is late - if so, replace them
                    for (Map.Entry<Integer, Long> e : heartbeatHistory.entrySet()) {
                        if ((System.currentTimeMillis() - e.getValue()) > 15 * 1000) {
                            LoggerClient.log("DANGER: RM AT PORT " + e.getKey().toString() + " HAS MISSED 3 HEARTBEATS");
                            replaceRM(e.getKey());
                        }
                    }
                LoggerClient.log("Current heartbeat history: " + heartbeatHistory.toString());
            }
        };

        // Create a replicaID
        int replicaID = new Random().nextInt(10000);


        // Start replica manager server, keep trying until we are on an empty port
        while(!replicaStarted) {
            UDP<MessageEnvelope> server = new UDP<>(port);
            try {
                MessageEnvelope message = new MessageEnvelope(new RegisterRmMessage(replicaID, port));
                UDP<MessageEnvelope> client = new UDP<>();
                client.send(message, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));

                // Start heartbeat thread
                hb = new Thread(() -> {
                    heartbeat();
                });
                hb.start();
                rmPort = port;
                server.startServer(callback);
                replicaStarted = true;
            } catch (BindException be) {
                // Bad port - stop heartbeat
                hb.interrupt();
                LoggerClient.log("bind excpeiton in rm");
                // Try on another port
                port += 1;
            }
        }


    }

    /**
     * Called when we need to replace RM
     * @param port to remove RM
     */
    public void replaceRM(int port) {
        // Stop heart beat for now
        hb.interrupt();
        LoggerClient.log("Removing port: " + port);
        Long removeID = null;
        // Remove port from list
        for (Map.Entry<Long, Integer> e : rmList.entrySet()) {
            if (e.getValue() == port) {
                removeID = e.getKey();
                break;
            }
        }
        synchronized (rmList) {
            if(removeID != null ) {
                rmList.remove(removeID);
            }
        }
        // Remove any history we have
        synchronized (heartbeatHistory) {
            heartbeatHistory.remove(port);
        }

        // Tell sequencer to remove the RM and start another one
        if(removeID != null) {
            MessageEnvelope me = new MessageEnvelope(new UnregisterRmMessage(removeID));
            UDP<MessageEnvelope> client = new UDP<>();
            LoggerClient.log("Telling sequencer to remove id");
            client.send(me, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));
        }
        // Restart heartbeat
        hb = new Thread(() -> {
            heartbeat();
        });
        hb.start();
    }


    /**
     * Send heartbeat message
     */
    public void heartbeat(){
        // Build message container
        MessageEnvelope me = new MessageEnvelope(new HeartbeatMessage(rmPort));
        UDP<MessageEnvelope> client = new UDP<>();
        try {
            while (true) {
                // Send to evvery RM we know about
                synchronized (rmList) {
                    for (int p : rmList.values()) {
                        LoggerClient.log("Sending heartbeat to RM at port: " + p);
                        client.send(me, "localhost", p);
                    }
                }
                // Know wait and do again
                Thread.sleep(5 * 1000);
            }
        } catch (InterruptedException e) {
            LoggerClient.log("heartbeat killed - probably because of inappropriate server start up");
        }
    }
}
