package at.rueckgr.chatbox.dto.message;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.io.Serializable;
import java.util.Collection;

public class NewMessagesNotification implements ChatboxNotification, Serializable {
    private static final long serialVersionUID = -4990339131103447543L;

    private final Collection<MessageDTO> newMessages;
    private final Collection<MessageDTO> modifiedMessages;

    public NewMessagesNotification(Collection<MessageDTO> newMessages, Collection<MessageDTO> modifiedMessages) {
        super();
        this.newMessages = newMessages;
        this.modifiedMessages = modifiedMessages;
    }

    public Collection<MessageDTO> getNewMessages() {
        return newMessages;
    }

    public Collection<MessageDTO> getModifiedMessages() {
        return modifiedMessages;
    }
}
