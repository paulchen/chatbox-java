package at.rueckgr.chatbox.dto;

import java.util.Set;


public interface MessageCache {
	public boolean contains(MessageDTO message);
	
	public void add(MessageDTO message);

	public Set<MessageDTO> getAllMessages();
}
