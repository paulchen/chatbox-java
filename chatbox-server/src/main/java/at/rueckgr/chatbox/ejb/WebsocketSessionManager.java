package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.util.Set;

public interface WebsocketSessionManager {
    void addSession(WebsocketEndpoint notifier);

    void removeSession(WebsocketEndpoint notifier);

    void newMessages(Set<MessageDTO> newMessages);

    void handleMessage(String message, WebsocketEndpoint session);
}
