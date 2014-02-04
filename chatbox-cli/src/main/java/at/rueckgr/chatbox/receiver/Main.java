package at.rueckgr.chatbox.receiver;

import java.io.Serializable;
import java.net.URI;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import at.rueckgr.chatbox.dto.message.FetchCurrentMessagesRequest;
import at.rueckgr.chatbox.util.GsonProcessor;
import at.rueckgr.chatbox.util.GsonProcessorImpl;

public class Main implements Serializable {
	private static final long serialVersionUID = 6695891896820029743L;

	public static void main(String[] args) throws Exception {
		final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
		ClientManager client = ClientManager.createClient();
		
		GsonProcessor gsonProcessor = new GsonProcessorImpl();
		
		Session session = client.connectToServer(new MessageReceiverEndpoint(), cec, new URI("ws://localhost:8080/chatbox-server-1.0-SNAPSHOT/websocket"));
		
		session.getBasicRemote().sendText(gsonProcessor.encode(new FetchCurrentMessagesRequest()));
		
		// TODO
		while(true) {
			Thread.sleep(1000);
		}
	}
}
