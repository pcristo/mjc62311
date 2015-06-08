package client;

import share.ShareType;

/**
 * Created by Ross on 2015-06-07.
 */
public class BatchSimulation {

    public static BrokerServiceClient client;
    public static void main(String args[]) throws Exception {
        runThread(ShareType.COMMON, "GOOG", "Spiderman");
        runThread(ShareType.COMMON, "MSFT", "Batman");
    }

    public static void runThread(ShareType type, String ticker, String name) throws Exception {
        client = new BrokerServiceClient();
        Simulation sim = new Simulation(client.getBroker(), type, ticker, name);
        Thread sim1 = new Thread(sim);
        sim1.start();
    }

}
