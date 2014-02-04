package at.rueckgr.chatbox.ejb;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ServerEndpoint("/websocket")
public class WebsocketEndpointImpl implements WebsocketEndpoint, Serializable {
	private static final long serialVersionUID = 3031583136358384436L;

	private Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	private WebsocketSessionManager sessionManager;

	private Session session;
	
	@OnOpen
	public void onOpen(final Session session) {
		this.sessionManager.addSession(this);
		this.session = session;
		
		log.info("New WebSocket session: " + session.getId());
	}
	
	@OnClose
	public void onClose(final CloseReason closeReason) {
		this.sessionManager.removeSession(this);
		
		log.info("WebSocket session closed: " + this.session.getId());
	}
	
	@OnMessage
	public void onMessage(final String message, final Session session) {
		log.info("Incoming WebSocket message from session " + this.session.getId());
		log.debug("Message: " + message);
		
		sessionManager.handleMessage(message, this);
	}
	
	@Override
	public void notify(String text) {
		log.debug(String.format("Notifying client %s with text %s", this.session.getId(), text));
		
		try {
			this.session.getBasicRemote().sendText(text);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
