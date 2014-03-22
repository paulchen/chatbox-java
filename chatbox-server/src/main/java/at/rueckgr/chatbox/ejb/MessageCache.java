package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.io.Serializable;
import java.util.Collection;


public interface MessageCache extends Serializable {
    // TODO magic number
    int CACHE_SIZE = 100;

    enum MessageStatus {
        UNMODIFIED,
        MODIFIED,
        NEW,
    }

    MessageStatus update(MessageDTO message);

    Collection<MessageDTO> getAllMessages();
}
