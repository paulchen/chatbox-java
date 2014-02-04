package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Singleton;

// TODO this annotation is sub-optimal, but required as the application does not work properly otherwise
@Singleton
public class MessageCacheImpl implements MessageCache, Serializable {
	private static final long serialVersionUID = 1586352196528965715L;
	
	private TreeSet<MessageDTO> messages = new TreeSet<MessageDTO>(new MessageSorter());
	
	@Override
	public synchronized boolean contains(MessageDTO message) {
		// TODO only compairs IDs; does not identify edits
		for(MessageDTO existingMessage : this.messages) {
			if(message.getId() == existingMessage.getId()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public synchronized void add(MessageDTO message) {
		if(this.contains(message)) {
			// TODO
			return;
		}
		
		messages.add(message);
		
		this.cleanup();
	}

	private void cleanup() {
		// TODO constant
		while(messages.size() > 100) {
			messages.remove(messages.first());
		}
	}

	@Override
	public Set<MessageDTO> getAllMessages() {
		return new TreeSet<MessageDTO>(this.messages);
	}
}
