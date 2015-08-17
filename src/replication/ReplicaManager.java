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


    public HashMap<Long, Integer> rmList = new HashMap<>();
    public Integer rmPort = null;
    private ConcurrentHashMap<Integer, Long> heartbeatHistory = new ConcurrentHashMap<Integer, Long>();
    public boolean heartbeat = true;

    Thread hb = null;

    public static void main(String[] args) {
        new ReplicaManager().start();
    }

    public void start() {


        Replica replica = new Replica();


        boolean replicaStarted = false;
        int port = 9998;
        Consumer<MessageEnvelope> callback = (MessageEnvelope me) -> {

            if(me.getType() == MessageType.OrderMessage) {
                // Handle actual message, send to replica to do with what he likes
                LoggerClient.log("Order Message received");
                replica.ToReceive(me);
            } else if(me.getType() == MessageType.PortNessage) {
                LoggerClient.log("Received port message");
                rmList = me.getPortMessage().getPorts();
                LoggerClient.log("Tracked RM's: " + rmList.toString());
            } else if(me.getType() == MessageType.HeartbeatMessage){
                LoggerClient.log("Heartbeat received from RM at port: " + me.getHeartbeatMessage().getPort().toString());
                    heartbeatHistory.put(me.getHeartbeatMessage().getPort(), System.currentTimeMillis());
                    // Check if anyone in history is late
                    for (Map.Entry<Integer, Long> e : heartbeatHistory.entrySet()) {
                        if ((System.currentTimeMillis() - e.getValue()) > 15 * 1000) {
                            LoggerClient.log("DANGER: RM AT PORT " + e.getKey().toString() + " HAS MISSED 3 HEARTBEATS");
                            replaceRM(e.getKey());
                        }
                    }
                LoggerClient.log("Current heartbeat history: " + heartbeatHistory.toString());
            }



        };

        int replicaID = new Random().nextInt(10000);

        // Start replica manager server
        while(!replicaStarted) {
            UDP<MessageEnvelope> server = new UDP<>(port);
            try {
                MessageEnvelope message = new MessageEnvelope(new RegisterRmMessage(replicaID, port));
                UDP<MessageEnvelope> client = new UDP<>();
                client.send(message, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));


                hb = new Thread(() -> {
                    heartbeat();
                });
                hb.start();
                rmPort = port;
                server.startServer(callback);
                replicaStarted = true;
            } catch (BindException be) {
                hb.interrupt();
                LoggerClient.log("bind excpeiton in rm");
                port += 1;
            }
        }


    }

    public void replaceRM(int port) {
        // Remove from list
        hb.interrupt();
        LoggerClient.log("Removing port: " + port);
        Long removeID = null;
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
        synchronized (heartbeatHistory) {
            heartbeatHistory.remove(port);
        }

        if(removeID != null) {
            MessageEnvelope me = new MessageEnvelope(new UnregisterRmMessage(removeID));
            UDP<MessageEnvelope> client = new UDP<>();
            LoggerClient.log("Telling sequencer to remove id");
            client.send(me, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));
        }
        hb = new Thread(() -> {
            heartbeat();
        });
        hb.start();

//        new Thread(() -> {
//            new ReplicaManager().start();
//        }).start();


    }


    public void heartbeat(){
        MessageEnvelope me = new MessageEnvelope(new HeartbeatMessage(rmPort));
        UDP<MessageEnvelope> client = new UDP<>();
        try {
            while (true) {
                synchronized (rmList) {
                    for (int p : rmList.values()) {
                        if(heartbeat) {
                            LoggerClient.log("Sending heartbeat to RM at port: " + p);
                            client.send(me, "localhost", p);
                        }
                    }
                }
                Thread.sleep(5 * 1000);
            }
        } catch (InterruptedException e) {
            LoggerClient.log("heartbeat killed - probably because of inappropriate server start up");
        }
    }


    public void setHeartbeat(Boolean b) {
        LoggerClient.log("Setting heartbeat to " + b.toString());
        heartbeat = b;
    }

}
