package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.dto.message.ChatboxMessage;

import java.io.Serializable;

public interface GsonProcessor extends Serializable {
    String encode(ChatboxMessage message);

    ChatboxMessage decode(String message);
}