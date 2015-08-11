package replication;

import common.Customer;
import common.share.ShareOrder;
import common.share.ShareType;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.OrderMessage;
import replication.messageObjects.OrderResponseMessage;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Gay on 8/9/2015.
 */
public class Replica {

    private Map<Long, OrderMessage> holdBack;
    private Long curSequence;
    private int replicaId;

    /**
     * Constructor for Replica
     * @param repId id assign by replica manager
     */
    public Replica(int repId) {

        this.replicaId = repId;
        holdBack = new TreeMap<Long, OrderMessage>();

        synchronized (curSequence) {

            curSequence = 0l;
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
    public MessageEnvelope receiveMessage(MessageEnvelope messageEnvelope){

        //Retrieve Order Message for enveloppe
        OrderMessage orderMessage = messageEnvelope.getOrderMessage();

        //Validate message queue vs current queue
        synchronized (curSequence) {

            if (orderMessage.getSequenceID() == curSequence + 1) {
                this.sendBuyRequest(orderMessage);
                curSequence = orderMessage.getSequenceID();
            } else {
                this.addToHoldBack(orderMessage);
            }

            return prepareConfirmation(true);
        }
    }

    /**
     *
     * @param orderMesage
     * @return
     */
    private boolean sendBuyRequest(OrderMessage orderMesage) {

        return false;
    }

    /**
     * Add message to hold back if not in sequence
     * @param orderMessage
     */
    private void addToHoldBack(OrderMessage orderMessage){

        //Does hold back have this message already
        if (this.holdBack.containsKey(orderMessage.getSequenceID())){
            //TODO: Send OrderResponseMessage
        } else {

            this.holdBack.put(orderMessage.getSequenceID(), orderMessage);

            //TODO: Sort HoldBack queue
        }
    }

    /**
     *
     * @param success
     * @return
     */
    private MessageEnvelope prepareConfirmation(boolean success){

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
    private void sendToExchange(OrderMessage orderMessage){

    }

    /**
     *
     * @param args
     */
    public static void main(String [] args) {

        Replica myReplica = new Replica(1);

        OrderMessage testOrder = new OrderMessage(1,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder1 = new OrderMessage(2,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder2 = new OrderMessage(3,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));
        OrderMessage testOrder3 = new OrderMessage(4,new ShareOrder("1","001","GOOG", ShareType.COMMON, 500F,50,500F), new Customer("Gay"));

        myReplica.addToHoldBack(testOrder2);
        myReplica.addToHoldBack(testOrder1);
        myReplica.addToHoldBack(testOrder3);
        myReplica.addToHoldBack(testOrder);

        System.out.println(myReplica.getHoldBack());


    }

}
