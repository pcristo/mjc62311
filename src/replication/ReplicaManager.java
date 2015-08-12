package replication;

import common.UDP;
import common.util.Config;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.RegisterRmMessage;

import java.util.function.Consumer;

public class ReplicaManager {

    public static void main(String[] args) {

        Replica replica = new Replica();

        MessageEnvelope message = new MessageEnvelope(new RegisterRmMessage(80085, 9997));
        UDP<MessageEnvelope> client = new UDP<>();
        client.send(message, "localhost", Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));

        UDP<MessageEnvelope> server = new UDP<>(9997);
        Consumer<MessageEnvelope> callback = (MessageEnvelope me) -> {
            System.out.println(me.getOrderMessage().getCustomer().getName());
            replica.ToReceive(me);
        };
        server.startServer(callback);




    }


}
