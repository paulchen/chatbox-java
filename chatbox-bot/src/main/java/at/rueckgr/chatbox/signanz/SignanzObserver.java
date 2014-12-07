package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.dto.events.NewMessagesEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class SignanzObserver {
    private @Inject BotService botService;

    public void newMessages(@Observes NewMessagesEvent newMessagesEvent) {
        if(!botService.isActive()) {
            return;
        }

        // TODO
    }
}
