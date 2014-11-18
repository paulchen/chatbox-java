package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.message.ChatboxMessage;
import at.rueckgr.chatbox.dto.message.FetchCurrentMessagesRequest;
import at.rueckgr.chatbox.dto.message.NewMessagesNotification;
import at.rueckgr.chatbox.service.events.NewMessagesEvent;
import at.rueckgr.chatbox.util.GsonProcessor;
import org.apache.commons.logging.Log;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Singleton
public class WebsocketSessionManager {
    private List<WebsocketEndpoint> sessions = new ArrayList<WebsocketEndpoint>();

    private @Inject MessageCache messageCache;
    private @Inject GsonProcessor gsonProcessor;
    private @Inject Log log;

    public void addSession(WebsocketEndpoint notifier) {
        sessions.add(notifier);
    }

    public void removeSession(WebsocketEndpoint notifier) {
        sessions.remove(notifier);
    }

    @Asynchronous
    public void newMessages(@Observes NewMessagesEvent newMessagesEvent) {
        log.debug("Notifying all clients about new and updated messages");

        NewMessagesNotification notification = new NewMessagesNotification(newMessagesEvent.getNewMessages(), newMessagesEvent.getModifiedMessages());
        String jsonMessage = gsonProcessor.encode(notification);

        for (WebsocketEndpoint notifier : sessions) {
            notifier.notify(jsonMessage);
        }
    }

    @Asynchronous
    public void handleMessage(String request, WebsocketEndpoint session) {
        ChatboxMessage message = gsonProcessor.decode(request);
        if (message instanceof FetchCurrentMessagesRequest) {
            log.debug("Processing FetchCurrentMessagesRequest");

            NewMessagesNotification newMessagesNotification =
                    new NewMessagesNotification(messageCache.getAllMessages(), new TreeSet<MessageDTO>());
            String jsonMessage = gsonProcessor.encode(newMessagesNotification);
            session.notify(jsonMessage);
        }
        // else {
            // TODO
        // }
    }
}
