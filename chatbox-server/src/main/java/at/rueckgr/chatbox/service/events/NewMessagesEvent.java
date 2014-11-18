package at.rueckgr.chatbox.service.events;

import at.rueckgr.chatbox.dto.MessageDTO;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

@Getter
public class NewMessagesEvent implements Serializable {
    private static final long serialVersionUID = 7643610067902232771L;

    private final Collection<MessageDTO> newMessages;
    private final Collection<MessageDTO> modifiedMessages;
    private final int totalMessagesCount;

    public NewMessagesEvent(Collection<MessageDTO> newMessages, Collection<MessageDTO> modifiedMessages, int totalMessagesCount) {
        this.newMessages = newMessages;
        this.modifiedMessages = modifiedMessages;
        this.totalMessagesCount = totalMessagesCount;
    }
}
