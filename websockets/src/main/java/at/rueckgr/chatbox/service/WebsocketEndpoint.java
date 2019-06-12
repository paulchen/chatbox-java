package at.rueckgr.chatbox.service;

import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;

@ServerEndpoint("/websocket")
public class WebsocketEndpoint implements Serializable {
    private static final long serialVersionUID = 3031583136358384436L;

    private @Inject Log log;
    private @Inject WebsocketSessionManager sessionManager;

    private transient Session session;

    @OnOpen
    public void onOpen(final Session session) {
        this.sessionManager.addSession(this);
        this.session = session;

        log.info("New WebSocket session: " + session.getId());
    }

    @OnClose
    public void onClose(final CloseReason closeReason) {
        this.sessionManager.removeSession(this);

        if (this.session != null) {
            log.info("WebSocket session closed: " + this.session.getId());
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session session) {
        if (this.session != null) {
            log.info("Incoming WebSocket message from session " + this.session.getId());
            log.debug("Message: " + message);

            sessionManager.handleMessage(message, this);
        }
    }

    public void notify(String text) {
        if (this.session != null) {
            log.debug(MessageFormat.format("Notifying client {0} with text {1}", this.session.getId(), text));

            try {
                this.session.getBasicRemote().sendText(text);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                throw ExceptionUtils.throwAsRuntimeException(e);
            }
        }
    }
}
