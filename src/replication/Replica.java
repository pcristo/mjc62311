package replication;

import common.Customer;
import common.UdpServer;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.OrderMessage;
import replication.messageObjects.OrderResponseMessage;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Gay on 8/9/2015.
 */
public class Replica extends UdpServer{

    private Map<Long, OrderMessage> holdBack;
    private Long curSequence = 0l;
    private static Integer uniqueID = 0;
    private int replicaId;


    /**
     * Replica Constructor
     */
    public Replica() {

        holdBack = new TreeMap<Long, OrderMessage>();
        synchronized (uniqueID){
            this.replicaId = uniqueID++;
        }

    }

    public Map<Long,OrderMessage> getHoldBack(){

        return this.holdBack;
    }

    /**
     *
     * @param messageEnvelope
     * @return
     */
    public void ToReceive(MessageEnvelope messageEnvelope){

        //Retrieve Order Message for envelope
        OrderMessage orderMessage = messageEnvelope.getOrderMessage();

        //Validate message queue vs current queue
        synchronized (curSequence) {

            if (orderMessage.getSequenceID() == curSequence + 1) {

                //Process request and sent message to front end
                this.ToDeliver(orderMessage);
                curSequence = orderMessage.getSequenceID();
                this.send(prepareMessage(true), "localhost", Config.getInstance().getAttr("FrontEndPort"));

                //Process Messages in HolbackQueue


            } else {
                //Add to Hold Back and wait
                this.addToHoldBack(orderMessage);
            }
        }
    }

    /**
     *
     * @param orderMessage
     * @return
     */
    private boolean sendBuyRequest(OrderMessage orderMessage) {

        return false;
    }

    /**
     * Add message to hold back if not in sequence
     * @param orderMessage
     */
    private void addToHoldBack(OrderMessage orderMessage){

        //Do not add duplicate messages to holdback queue
        if (!this.holdBack.containsKey(orderMessage.getSequenceID())){
            this.holdBack.put(orderMessage.getSequenceID(), orderMessage);
        }
    }

    /**
     *
     * @param success
     * @return
     */
    private MessageEnvelope prepareMessage(boolean success){

        synchronized (curSequence) {
            OrderResponseMessage response = new OrderResponseMessage(curSequence,replicaId,success);
            MessageEnvelope messageEnvelope = new MessageEnvelope(response);

            return messageEnvelope;
        }
    }

    /**
     *
     * @param orderMessage
     */
    private void ToDeliver(OrderMessage orderMessage){

        //TODO: Send Order request to Exchange Server
    }


    public int getReplicaId() {
        return this.replicaId;
    }

    @Override
    public void incomingMessageHandler(MessageEnvelope me) {

    }

    /**
     *
     * @param args
     */
    public static void main(String [] args) {

        Replica myReplica = new Replica();
        Replica myReplica2 = new Replica();
        Replica myReplica3 = new Replica();


       /* OrderMessage testOrder = new OrderMessage(1,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder1 = new OrderMessage(2,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder2 = new OrderMessage(3,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder3 = new OrderMessage(4,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));

        myReplica.addToHoldBack(testOrder2);
        myReplica.addToHoldBack(testOrder1);
        myReplica.addToHoldBack(testOrder3);
        myReplica.addToHoldBack(testOrder);*/

        System.out.println(myReplica.getReplicaId());
        System.out.println(myReplica2.getReplicaId());
        System.out.println(myReplica3.getReplicaId());


    }


}
