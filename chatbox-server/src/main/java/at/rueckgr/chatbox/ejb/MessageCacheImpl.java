package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.MessageId;
import at.rueckgr.chatbox.dto.MessageIdSorter;

import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

@Singleton
public class MessageCacheImpl implements MessageCache, Serializable {
    private static final long serialVersionUID = 1586352196528965715L;

    private Map<MessageId, MessageDTO> messages = Collections.synchronizedMap(new TreeMap<MessageId, MessageDTO>(new MessageIdSorter()));

    private MessageStatus contains(MessageDTO message) {
        if(!this.messages.containsKey(message.getMessageId())) {
            return MessageStatus.NEW;
        }
        if(this.messages.get(message.getMessageId()).equalsRaw(message)) {
            return MessageStatus.UNMODIFIED;
        }

        return MessageStatus.MODIFIED;
    }

    @Override
    public MessageStatus update(MessageDTO message) {
        MessageStatus messageStatus = this.contains(message);

        if(messageStatus != MessageStatus.UNMODIFIED) {
            this.messages.put(message.getMessageId(), message);
        }

        this.cleanup();

        return messageStatus;
    }

    private void cleanup() {
        while (messages.size() > CACHE_SIZE) {
            messages.remove(messages.keySet().iterator().next());
        }
    }

    @Override
    public Collection<MessageDTO> getAllMessages() {
        return new TreeSet<MessageDTO>(this.messages.values());
    }
}
