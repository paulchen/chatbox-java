package at.rueckgr.chatbox.ejb;

import java.util.Set;

import at.rueckgr.chatbox.dto.MessageDTO;

public interface WebsocketSessionManager {
	public void addSession(WebsocketEndpoint notifier);
	
	public void removeSession(WebsocketEndpoint notifier);

	public void newMessages(Set<MessageDTO> newMessages);

	public void handleMessage(String message, WebsocketEndpoint session);
}
