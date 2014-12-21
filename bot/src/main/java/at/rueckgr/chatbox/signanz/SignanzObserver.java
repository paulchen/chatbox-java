package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.events.NewMessagesEvent;
import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.signanz.responder.ResponderPlugin;
import at.rueckgr.chatbox.signanz.responder.ResponderResult;
import at.rueckgr.chatbox.util.DependencyHelper;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class SignanzObserver {
    private @Inject BotService botService;
    private @Inject DependencyHelper dependencyHelper;
    private @Inject MailService mailService;

    private List<Class<? extends Plugin>> responders;

    @PostConstruct
    public void init() {
        responders = dependencyHelper.getPluginTypes(ResponderPlugin.class);

    }

    public void newMessages(@Observes NewMessagesEvent newMessagesEvent) {
        if(!botService.isActive()) {
            return;
        }

        List<String> messagesToPost = new ArrayList<String>();

        mergeMessages(newMessagesEvent).forEach(messageDTO -> {
            for (Class<? extends Plugin> responder : responders) {
                ResponderPlugin plugin = (ResponderPlugin) BeanProvider.getContextualReference(responder);
                ResponderResult responderResult = plugin.processMessage(messageDTO);

                if(responderResult != null) {
                    if(responderResult.getMessage() != null) {
                        messageDTO.setMessage(responderResult.getMessage());
                    }

                    if(responderResult.getMessagesToPost() != null) {
                        messagesToPost.addAll(responderResult.getMessagesToPost());
                    }
                }
            }
        });

        messagesToPost.forEach(botService::post);
    }

    private Stream<MessageDTO> mergeMessages(NewMessagesEvent newMessagesEvent) {
        List<MessageDTO> result = new ArrayList<MessageDTO>();

        result.addAll(newMessagesEvent.getNewMessages());
        result.addAll(newMessagesEvent.getModifiedMessages());

        return result.stream()
                .sorted((m1, m2) -> m1.getPrimaryId().compareTo(m2.getPrimaryId()));
    }
}
