package replication;

import common.UdpServer;
import common.util.Config;
import org.junit.Test;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.RegisterRmMessage;

import static org.junit.Assert.assertTrue;

public class FrontEndTest extends UdpServer {
	@Test
	public void SendMessage() throws InterruptedException {
        // Create client object
        @SuppressWarnings("unused")
		FrontEnd frontEnd = new FrontEnd();
		FrontEndTest client = new FrontEndTest();
        client.launch(12483);
        
        Thread.sleep(1000);
        
        // Create object we want to send
        RegisterRmMessage rRM = new RegisterRmMessage(9995, 9995);

        // Send to server
        MessageEnvelope me = new MessageEnvelope(rRM);
        assertTrue(client.send(me, "localhost", 
        		Integer.parseInt(Config.getInstance().getAttr("FrontEndPort"))));
	}

	@Override
	protected void incomingMessageHandler(MessageEnvelope me) {
		return;
	}	
	
}
