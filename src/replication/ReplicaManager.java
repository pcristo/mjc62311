package replication;

import common.UDP;
import common.logger.LoggerClient;
import common.util.Config;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.RegisterRmMessage;
import replication.messageObjects.UnregisterRmMessage;

import java.net.BindException;
import java.util.Random;
import java.util.function.Consumer;

public class ReplicaManager {

    public static void main(String[] args) {

        Replica replica = new Replica();



        boolean replicaStarted = false;
        int port = 9998;
        Consumer<MessageEnvelope> callback = (MessageEnvelope me) -> {
            LoggerClient.log("Message received");
            replica.ToReceive(me);
        };

        int replicaID = new Random().nextInt(10000);

        while(!replicaStarted) {
            UDP<MessageEnvelope> server = new UDP<>(port);
            try {


                MessageEnvelope message = new MessageEnvelope(new RegisterRmMessage(replicaID, port));
                UDP<MessageEnvelope> client = new UDP<>();
                client.send(message, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));

                server.startServer(callback);
                replicaStarted = true;
            } catch (BindException be) {
                LoggerClient.log("bind excpeiton in rm");


                MessageEnvelope message = new MessageEnvelope(new UnregisterRmMessage(replicaID));
                UDP<MessageEnvelope> client = new UDP<>();
                client.send(message, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));

                port += 1;
            }
        }





    }


}
