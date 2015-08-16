package replication;

import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import WebServices.ExchangeClientServices.ShareType;
import business.BusinessWSPublisher;
import common.Customer;
import common.UDP;
import common.UdpServer;
import common.logger.LoggerClient;
import common.share.ShareOrder;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.OrderMessage;
import replication.messageObjects.OrderResponseMessage;
import stockexchange.exchange.ExchangeWSPublisher;

import java.net.BindException;
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
    private String exhcnagePort;
    private String businessPort;


    /**
     * Replica Constructor
     */
    public Replica() {

        boolean exchangeStarted = false;
        Integer port = 8888;
        String[] arr = new String[1];
        while (!exchangeStarted) {
            try {
                arr[0] = port.toString();
                ExchangeWSPublisher.main(arr);
                exchangeStarted = true;
            } catch (BindException be) {
                port += 1;
            }
        }
        exhcnagePort = port.toString();
        port = 18001;
        arr = new String[2];
        boolean businessStarted = false;
        while(!businessStarted) {
            try {
                arr[0] = port.toString();
                arr[1] = exhcnagePort;
                BusinessWSPublisher.main(arr);
                businessStarted = true;
            } catch(Exception e) {
                port += 1;
            }
        }
        businessPort = port.toString();

        holdBack = new TreeMap<Long, OrderMessage>();
        synchronized (uniqueID){
            this.replicaId = ++uniqueID;
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

        int returnPort = orderMessage.getReturnPort();

        LoggerClient.log("Replica FE return port is : " + returnPort);

        //Validate message queue vs current queue
        synchronized (curSequence) {

            if (orderMessage.getSequenceID() == curSequence + 1) {

                //Process request and sent message to front end
                try {
                    if (this.ToDeliver(orderMessage)) {
                        LoggerClient.log("TODeliver success - returning to front end on port " + returnPort);

                        curSequence = orderMessage.getSequenceID();
                        sendConfirmation(true, returnPort);

                        //Process Messages in HolbackQueue
                        processHoldback();

                    } else {
                        // Unable to make share purchase
                        LoggerClient.log("TODeliver fail - returning to front end on port " + returnPort);
                        sendConfirmation(false, returnPort);
                        curSequence = orderMessage.getSequenceID();
                        processHoldback();
                    }
                } catch (Exception e) {
                    LoggerClient.log("Error in message delivery : " + e.getMessage());
                    sendConfirmation(false, returnPort);
                }
            } else {
                LoggerClient.log("Adding message to hold back queue");
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

    private void processHoldback() {

        for(Map.Entry<Long, OrderMessage> entry : holdBack.entrySet()) {
            Long key = entry.getKey();
            OrderMessage om = entry.getValue();

            synchronized (this.curSequence){

                if (om.getSequenceID() == this.curSequence) {

                    try {
                        if (this.ToDeliver(om)) {
                            curSequence = om.getSequenceID();
                            sendConfirmation(true, om.getReturnPort());
                        }

                    } catch (Exception e) {
                        sendConfirmation(false, om.getReturnPort());
                        LoggerClient.log("Error in HoldBack queue message delivery : " + e.getMessage());
                    }
                }
            }
        }
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
    private void sendConfirmation(boolean success, int returnPort){

        synchronized (curSequence) {
            OrderResponseMessage response = new OrderResponseMessage(curSequence,replicaId,success);
            UDP<OrderResponseMessage> client = new UDP<>();
            client.send(response, "localhost", returnPort);
        }
    }

    /**
     *
     * @param orderMessage
     */
    private boolean ToDeliver (OrderMessage orderMessage) throws Exception{

        String exName;

       if (replicaId == 1)
       {
           exName = "TSX";
       }
        else
       {
           exName = "TSXCOPY" + replicaId;
       }

        ExchangeWSImplService exchangews = new ExchangeWSImplService(exName, exhcnagePort);
        IExchange exchange = exchangews.getExchangeWSImplPort();

        ShareOrder sOrder = orderMessage.getShareOrder();
        Customer customer = orderMessage.getCustomer();

        ShareItem sItem = new ShareItem();
        sItem.setOrderNum(sOrder.getOrderNum());
        sItem.setBusinessSymbol(sOrder.getBusinessSymbol());
        sItem.setShareType(ShareType.COMMON);
        sItem.setQuantity(sOrder.getQuantity());
        sItem.setUnitPrice(sOrder.getUnitPrice());

        WebServices.ExchangeClientServices.Customer orderCust = new WebServices.ExchangeClientServices.Customer();
        orderCust.setName(customer.getName());

        boolean response = exchange.sellShareService(sItem, orderCust);


        return response;

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
