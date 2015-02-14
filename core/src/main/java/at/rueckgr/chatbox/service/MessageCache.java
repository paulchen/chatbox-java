package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.dto.MessageDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

@ApplicationScoped
public class MessageCache {
    // TODO magic number
    public static final int CACHE_SIZE = 100;

    // TODO move this enum to somewhere else
    public enum MessageStatus {
        UNMODIFIED,
        MODIFIED,
        NEW,
    }

    private Map<Integer, MessageDTO> messages = Collections.synchronizedMap(new TreeMap<Integer, MessageDTO>());

    private MessageStatus contains(MessageDTO message) {
        if(!this.messages.containsKey(message.getPrimaryId())) {
            return MessageStatus.NEW;
        }
        if (isModified(this.messages.get(message.getPrimaryId()), message)) {
            return MessageStatus.MODIFIED;
        }

        return MessageStatus.UNMODIFIED;
    }

    private boolean isModified(MessageDTO message1, MessageDTO message2) {
        if(!message1.getId().equals(message2.getId())) {
            return false;
        }
        if(!message1.getEpoch().equals(message2.getEpoch())) {
            return false;
        }
        if(!message1.getDate().equals(message2.getDate())) {
            return false;
        }
        if(!message1.getRawMessage().equals(message2.getRawMessage())) {
            return false;
        }
        if(message1.getUser().getId() != message2.getUser().getId()) {
            return false;
        }

        return true;
    }

    public MessageStatus update(MessageDTO message) {
        MessageStatus messageStatus = this.contains(message);

        if(messageStatus != MessageStatus.UNMODIFIED) {
            this.messages.put(message.getPrimaryId(), message);
        }

        this.cleanup();

        return messageStatus;
    }

    private void cleanup() {
        while (messages.size() > CACHE_SIZE) {
            messages.remove(messages.keySet().iterator().next());
        }
    }

    public Collection<MessageDTO> getAllMessages() {
        return new TreeSet<MessageDTO>(this.messages.values());
    }

    public int getMaxShoutId() {
        Integer maxId = null;
        for (Integer id : messages.keySet()) {
            if(maxId == null || maxId < id) {
                maxId = id;
            }
        }

        return maxId;
    }
}
