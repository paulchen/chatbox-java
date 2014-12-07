package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.service.database.SettingsService;
import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@ApplicationScoped
public class LoveResponder extends AbstractResponderPlugin {
    private @Inject Log log;
    private @Inject SettingsService settingsService;

    private String lovePattern;

    @PostConstruct
    public void init() {
        String username = settingsService.getSetting(Setting.FORUM_USERNAME);

        lovePattern = MessageFormat.format("\\s*{0}\\s*:inlove:\\s*", Pattern.quote(username));
    }

    @Override
    public ResponderResult processMessage(MessageDTO messageDTO) {
        String message = messageDTO.getMessage();

        List<String> messagesToPost = new ArrayList<String>();
        if(message.matches(lovePattern)) {
            messagesToPost.add(MessageFormat.format("{0} :inlove:", messageDTO.getUser().getName()));
        }

        return new ResponderResult(message, messagesToPost);
    }
}
