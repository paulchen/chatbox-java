package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageCache;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.message.ChatboxMessage;
import at.rueckgr.chatbox.dto.message.FetchCurrentMessagesRequest;
import at.rueckgr.chatbox.dto.message.NewMessagesNotification;
import at.rueckgr.chatbox.util.GsonProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class WebsocketSessionManagerImpl implements WebsocketSessionManager, Serializable {
    private static final long serialVersionUID = -3564796548736536517L;

    private Log log = LogFactory.getLog(this.getClass());

    private List<WebsocketEndpoint> sessions = new ArrayList<WebsocketEndpoint>();

    @Inject
    private MessageCache messageCache;

    @Inject
    private GsonProcessor gsonProcessor;

    @Override
    public void addSession(WebsocketEndpoint notifier) {
        sessions.add(notifier);
    }

    @Override
    public void removeSession(WebsocketEndpoint notifier) {
        sessions.remove(notifier);
    }

    @Asynchronous
    @Override
    public void newMessages(Set<MessageDTO> newMessages) {
        log.debug("Notifying all clients about new messages");

        String jsonMessage = gsonProcessor.encode(new NewMessagesNotification(newMessages));

        for (WebsocketEndpoint notifier : sessions) {
            notifier.notify(jsonMessage);
        }
    }

    @Asynchronous
    @Override
    public void handleMessage(String request, WebsocketEndpoint session) {
        ChatboxMessage message = gsonProcessor.decode(request);
        if (message instanceof FetchCurrentMessagesRequest) {
            log.debug("Processing FetchCurrentMessagesRequest");

            String jsonMessage = gsonProcessor.encode(new NewMessagesNotification(messageCache.getAllMessages()));
            session.notify(jsonMessage);
        }
        // else {
            // TODO
        // }
    }
}
