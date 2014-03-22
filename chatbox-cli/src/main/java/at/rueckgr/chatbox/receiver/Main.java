package at.rueckgr.chatbox.receiver;

import at.rueckgr.chatbox.dto.message.FetchCurrentMessagesRequest;
import at.rueckgr.chatbox.util.GsonProcessor;
import at.rueckgr.chatbox.util.GsonProcessorImpl;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Session;
import java.io.Serializable;
import java.net.URI;
import java.util.Scanner;

public class Main implements Serializable {
    private static final long serialVersionUID = 6695891896820029743L;

    public static void main(String[] args) throws Exception {
        System.out.println("Startup...");

        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();

        GsonProcessor gsonProcessor = new GsonProcessorImpl();

        Session session = client.connectToServer(new MessageReceiverEndpoint(), cec, new URI("ws://localhost:8080/chatbox-server-1.0-SNAPSHOT/websocket"));

        session.getBasicRemote().sendText(gsonProcessor.encode(new FetchCurrentMessagesRequest()));

        System.out.println("Startup complete. This client keeps running until you type 'quit'.");

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("quit")) {
                break;
            }
        }

        System.out.println("Shutdown...");

        session.close();
    }
}
