package at.rueckgr.chatbox.receiver;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.Serializable;

public class MessageReceiverEndpoint extends Endpoint implements Serializable {
    private static final long serialVersionUID = 8245849681999271413L;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler(new ChatboxMessageHandler());
    }
}
