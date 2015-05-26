package client;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sai on 2015/5/25.
 */
public class ServerDisplayMsgs {
    private final static String WELCOME = "Welcome using the share exchange service!\nPlease select desired operation:\n";
    private final static String SEPARATOR = "*************************************************\n*************************************************";
    private final static String OPERATIONS = "1.Sell\n2.Buy\n3.Check listing info\n4.Exit";
    private final static String OPTIONS = "1.Add a ticker\n2.remove a ticker\n3.Continue";
    private final static String SELECTION = "Please enter selection:";
    private final static String TICKER = "Please enter ticker:";
    private final static String TICKERTYPE = "Please enter desired ticker type(common,preferred,convertible):";
    private final static String TICKERQUATITY = "Please enter desired ticker quantity:";

    public static void printWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(WELCOME);
        System.out.println(SEPARATOR);
    }

    public static int printOps() {
        System.out.println(SEPARATOR);
        System.out.println(OPERATIONS);
        System.out.println(SEPARATOR);
        System.out.println(SELECTION);
        Scanner scan = new Scanner(System.in);
        int option = scan.nextInt();
        return option;
    }

    public static int printOptions() {
        System.out.println(SEPARATOR);
        System.out.println(OPTIONS);
        System.out.println(SEPARATOR);
        System.out.println(SELECTION);
        Scanner scan = new Scanner(System.in);
        int option = scan.nextInt();
        return option;
    }

    public static String enterTicker() {
        System.out.println(TICKER);
        Scanner scan = new Scanner(System.in);
        String ticker = scan.nextLine();
        return ticker;
    }

    public static ArrayList<String> getTickerList() {
        ArrayList<String> list = new ArrayList<String>();
        boolean stop = false;
        while (!stop) {
            switch (printOptions()) {
                case 1:
                    list.add(enterTicker());
                    printList("Tickers:\n", list);
                    break;
                case 2:
                    if (list.size() == 0) {
                        System.out.println("No ticker available");
                        break;
                    } else {
                        System.out.println("Enter index of ticker to be removed: ");
                        Scanner scan = new Scanner(System.in);
                        int index = scan.nextInt();
                        list.remove(index - 1);
                        break;
                    }

                case 3:
                    stop = true;
                    break;
            }

        }
        return list;
    }

    public static void printResult(boolean ret) {
        System.out.println(SEPARATOR);
        if (ret) {
            System.out.println("Operation successful");
        } else {
            System.out.println("Operation failed");
        }
        System.out.println(SEPARATOR);
    }

    public static String enterTickerType() {
        System.out.println(TICKERTYPE);
        Scanner scan = new Scanner(System.in);
        String ticker = scan.nextLine();
        return ticker;
    }

    public static void flushConsole() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static int enterTickerQuantity() {
        System.out.println(TICKERQUATITY);

        Scanner scan = new Scanner(System.in);
        int quantity = scan.nextInt();
        return quantity;
    }

    public static void printList(String title, ArrayList<String> list) {
        System.out.println(SEPARATOR);
        System.out.println(title);
        for (String item : list) {
            System.out.println((list.indexOf(item) + 1) + ". " + item);
        }
        System.out.println(SEPARATOR);
    }

    public static String getCustomerInfo() {
        Scanner scan = new Scanner(System.in);
        String info = "";
        System.out.println(SEPARATOR);
        System.out.println("Please enter customer reference number: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter customer name: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter street1: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter street2: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter city: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter province: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter postal code: ");
        info += scan.nextLine();
        info += ";;d";
        System.out.println("Please enter contry: ");
        info += scan.nextLine();

        System.out.println(SEPARATOR);
        return info;
    }
}
