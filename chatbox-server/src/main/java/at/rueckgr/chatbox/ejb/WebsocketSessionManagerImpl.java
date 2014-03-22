package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.message.ChatboxMessage;
import at.rueckgr.chatbox.dto.message.FetchCurrentMessagesRequest;
import at.rueckgr.chatbox.dto.message.NewMessagesNotification;
import at.rueckgr.chatbox.util.GsonProcessor;
import org.apache.commons.logging.Log;

import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ApplicationScoped
public class WebsocketSessionManagerImpl implements WebsocketSessionManager, Serializable {
    private static final long serialVersionUID = -3564796548736536517L;

    private List<WebsocketEndpoint> sessions = new ArrayList<WebsocketEndpoint>();

    @Inject
    private MessageCache messageCache;

    @Inject
    private GsonProcessor gsonProcessor;

    @Inject
    private Log log;

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
    public void newMessages(Set<MessageDTO> newMessages, Set<MessageDTO> modifiedMessages) {
        log.debug("Notifying all clients about new and updated messages");

        String jsonMessage = gsonProcessor.encode(new NewMessagesNotification(newMessages, modifiedMessages));

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

            NewMessagesNotification newMessagesNotification = new NewMessagesNotification(messageCache.getAllMessages(), new TreeSet<MessageDTO>());
            String jsonMessage = gsonProcessor.encode(newMessagesNotification);
            session.notify(jsonMessage);
        }
        // else {
            // TODO
        // }
    }
}
