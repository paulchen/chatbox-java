package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.util.Comparator;

public class MessageSorter implements Comparator<MessageDTO>, Serializable {
	private static final long serialVersionUID = -9158666393225705859L;

	@Override
	public int compare(MessageDTO message1, MessageDTO message2) {
		if(message1.getEpoch() != message2.getEpoch()) {
			return Integer.compare(message1.getEpoch(), message2.getEpoch());
		}
		return Integer.compare(message1.getId(), message2.getId());
	}
}

