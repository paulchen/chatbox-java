package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.util.Set;


public interface MessageCache extends Serializable {
    boolean contains(MessageDTO message);

    void add(MessageDTO message);

    Set<MessageDTO> getAllMessages();
}
