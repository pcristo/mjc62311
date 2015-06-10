package client;

import logger.TimerLoggerClient;
import share.ShareType;

import java.util.ArrayList;
import java.util.Random;


public class BatchSimulation {

    public static BrokerServiceClient client;
    public static void main(String args[]) throws Exception {
        TimerLoggerClient tlc = new TimerLoggerClient();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        threads.addAll(runBatchPurchase());
      //  threads.addAll(runBatchPurchase());
       // threads.addAll(runBatchPurchase());



    }

    /**
     * Start a group thread to create 1000 share orders running simultaneously
     */
    public static ArrayList<Thread> runBatchPurchase() {
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        (new Thread() {
            public void run() {
                for(int i = 0; i<1000; ++i) {
                    int nameIndex = new Random().nextInt(names.length);
                    int stockIndex = new Random().nextInt(stocks.length);
                    try {
                        Thread running = BatchSimulation.runPurchaseThread(ShareType.COMMON, stocks[stockIndex], names[nameIndex]);
                        threadList.add(running);
                    } catch(Exception e){
                        System.out.println("ERROR");
                    }
                }
            }
        }).start();
        return threadList;
    }

    /**
     * Wrapper for creating purchase shares thread
     *
     * @param type ShareTypeof share
     * @param ticker String symbol
     * @param name String of customer
     * @return
     * @throws Exception
     */
    public static Thread runPurchaseThread(ShareType type, String ticker, String name) throws Exception {
        client = new BrokerServiceClient();
        Simulation sim = new Simulation(client.getBroker(), type, ticker, name);
        Thread sim1 = new Thread(sim);
        sim1.start();
        return sim1;
    }

    /**
     * List of thread names
     */
    public static String[] names = {
            "Spiderman",
            "Superman",
            "Antman",
            "Batwoman",
            "Venom",
            "Joker",
            "Batman",
            "Woderwoman",
            "Human Torch",
            "Flash",
            "Black Widow",
            "Quick Silver",
            "Birdman",
            "Blizzard",
            "Carnage",
            "Falcon"
    };

    /**
     * List of stock tickers
     */
    public static String[] stocks = {
            "GOOG",
            "MSFT",
            "YHOO"
    };

}
