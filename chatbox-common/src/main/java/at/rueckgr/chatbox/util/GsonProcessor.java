package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.dto.message.ChatboxMessage;

public interface GsonProcessor {

	public abstract String encode(ChatboxMessage message);

	public abstract ChatboxMessage decode(String message);

}