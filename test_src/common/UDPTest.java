package common;

import common.logger.LoggerServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.BindException;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertTrue;

public class UDPTest {

    UDP<Customer> udp;
    Thread logger;


    // HOW TO START THE UDP SERVER
    public void startServerExample() {
        // Let presume you want to send a customer object.
        // Make sure Customer implements serializable

        // Create server object
        @SuppressWarnings("unused") // don't need to use the object, creating is the action
		UDP<Customer> server = new UDP<>();

        // Tell the server what to do when it recieves the object
        // This is done via lambda Consumer
        @SuppressWarnings("unused") // we don't need to use this object, creating it is the action
		Consumer<Customer> callableFunction = (Customer c) -> {
            // Do what you want with the object
            c.setName("Bob");
            // blah blah.
        };

        // Start the server
        //server.startServer(callableFunction);
    }

    // HOW TO SEND AN OBJECT TO UDP SERVER
    public void sendMessageExample() {
        // We have the UDP server running, expecting a customer object

        // Create client object
        UDP<Customer> client = new UDP<>();
        // Create object we want to send
        Customer ross = new Customer("Ross");

        // Send to server
        client.send(ross);
    }
    
    @Before
    public void setUP() {
        udp = new UDP<Customer>();
        logger = new Thread(() -> {
            LoggerServer.main(null);
        });
        logger.start();

    }

    @After
    public void tearDown() {
        udp = null;
        logger.interrupt();
    }
    
    @Test
    public void sendTest() {

        new Thread(() -> {
            Consumer<Customer> callable = (Customer c) -> {
                System.out.println("HI");
                System.out.println(c.getName());
            };

            try {
                new UDP<Customer>().startServer(callable);
            } catch (BindException be) {
                System.out.println("Error in UDP Test : " + be.getMessage());
            }

        }).start();
        assertTrue(udp.send(new Customer("Ross")));


    }
}
