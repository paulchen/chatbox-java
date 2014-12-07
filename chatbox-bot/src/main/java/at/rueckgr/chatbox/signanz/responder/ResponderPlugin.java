package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.dto.MessageDTO;

public interface ResponderPlugin extends Plugin {
    ResponderResult processMessage(MessageDTO messageDTO);
}
