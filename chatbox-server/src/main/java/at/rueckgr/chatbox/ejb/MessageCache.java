package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.io.Serializable;
import java.util.Set;


public interface MessageCache extends Serializable {
    boolean contains(MessageDTO message);

    void add(MessageDTO message);

    Set<MessageDTO> getAllMessages();
}
