package at.rueckgr.chatbox.dto.message;

import java.io.Serializable;
import java.util.Set;

import at.rueckgr.chatbox.dto.MessageDTO;

public class NewMessagesNotification implements ChatboxNotification, Serializable {
	private static final long serialVersionUID = -4990339131103447543L;
	
	private final Set<MessageDTO> messages;

	public NewMessagesNotification(Set<MessageDTO> messages) {
		super();
		this.messages = messages;
	}

	public Set<MessageDTO> getMessages() {
		return messages;
	}
}
