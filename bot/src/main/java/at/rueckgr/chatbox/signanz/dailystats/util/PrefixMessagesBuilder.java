package at.rueckgr.chatbox.signanz.dailystats.util;

import at.rueckgr.chatbox.signanz.dailystats.prefix.PrefixPlugin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PrefixMessagesBuilder {
    private @Inject MessagesBuilder messagesBuilder;

    public List<? extends BuilderResult> buildMessages() {
        return messagesBuilder.buildMessages(PrefixPlugin.class, plugin -> {
            if(plugin.isActive()) {
                return new BuilderResult(plugin.getMessage());
            }
            return null;
        });
    }
}
