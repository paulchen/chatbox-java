package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.dto.message.ChatboxMessage;

public interface GsonProcessor {
    String encode(ChatboxMessage message);

    ChatboxMessage decode(String message);
}