package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author paulchen
 */
// TODO do we need this class at all?
public class MessageSorter implements Comparator<MessageDTO>, Serializable {

    private static final long serialVersionUID = -3421364667198698086L;

    @Override
    public int compare(MessageDTO message1, MessageDTO message2) {
        // TODO what if message1 or message2 is null?
        return message1.compareTo(message2);
    }
}
