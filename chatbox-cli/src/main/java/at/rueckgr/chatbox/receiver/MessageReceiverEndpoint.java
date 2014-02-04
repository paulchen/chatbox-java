package at.rueckgr.chatbox.receiver;

import java.io.Serializable;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class MessageReceiverEndpoint extends Endpoint implements Serializable {
	private static final long serialVersionUID = 8245849681999271413L;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(new ChatboxMessagehandler());
	}
}
