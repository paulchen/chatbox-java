package at.rueckgr.chatbox.signanz.dailystats.util;

import at.rueckgr.chatbox.signanz.dailystats.suffix.SuffixPlugin;

import javax.inject.Inject;
import java.util.List;

public class SuffixMessagesBuilder {
    private @Inject MessagesBuilder messagesBuilder;

    public List<BuilderResult> buildMessages() {
        return messagesBuilder.buildMessages(SuffixPlugin.class, plugin -> {
            if (plugin.isActive()) {
                return new BuilderResult(plugin.getMessage());
            }
            return null;
        });
    }
}
