package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.dto.events.NewMessagesEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class SignanzObserver {
    public void newMessages(@Observes NewMessagesEvent newMessagesEvent) {
        // TODO
    }
}
