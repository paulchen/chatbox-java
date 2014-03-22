package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author paulchen
 */
public class MessageSorter implements Comparator<MessageDTO>, Serializable {
    private static final long serialVersionUID = -3421364667198698086L;

    private final MessageIdSorter messageIdSorter = new MessageIdSorter();

    @Override
    public int compare(MessageDTO message1, MessageDTO message2) {
        // TODO what if message1 or message2 is null?
        return messageIdSorter.compare(message1.getMessageId(), message2.getMessageId());
    }
}
