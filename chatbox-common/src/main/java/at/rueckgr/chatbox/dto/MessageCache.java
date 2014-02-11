package at.rueckgr.chatbox.dto;

import java.util.Set;


public interface MessageCache {
    boolean contains(MessageDTO message);

    void add(MessageDTO message);

    Set<MessageDTO> getAllMessages();
}
