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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class GreetingResponder extends AbstractResponderPlugin {
    private static final String[][] BROHOOF_REPLIES = {
            {"/)", "(\\"},
            {"(\\", "/)"},
            {"/]", "[\\"},
            {"[\\", "/]"},
            {"(\\ ^ . ^ /)", "(\\ ^ . ^ /)"},
            {"<3", "<3"},
    };

    private @Inject Log log;
    private @Inject SettingsService settingsService;

    private String lovePattern;
    private String brohoofPattern;
    private String troestPattern;

    @PostConstruct
    public void init() {
        String username = settingsService.getSetting(Setting.FORUM_USERNAME);

        lovePattern = MessageFormat.format("\\s*{0}\\s*:(inlove|druegg|\\^101|knutsch):\\s*", Pattern.quote(username));
        brohoofPattern = MessageFormat.format("\\s*{0}\\s*([/\\\\\\(\\)\\[\\]\\.\\^<3 ]+)\\s*", Pattern.quote(username));
        troestPattern = MessageFormat.format("\\s*{0}\\s*:(traurig):\\s*", Pattern.quote(username));
    }

    @Override
    public ResponderResult processMessage(MessageDTO messageDTO) {
        String message = messageDTO.getMessage();
        String username = messageDTO.getUser().getName();

        List<String> messagesToPost = new ArrayList<String>();
        checkLovePattern(messagesToPost, message, username);
        checkBrohoofPattern(messagesToPost, message, username);
        checkTroestPattern(messagesToPost, message, username);

        return new ResponderResult(message, messagesToPost);
    }

    private void checkLovePattern(List<String> messagesToPost, String message, String username) {
        Pattern pattern = Pattern.compile(lovePattern);
        Matcher matcher = pattern.matcher(message);
        if(matcher.matches()) {
            messagesToPost.add(MessageFormat.format("{0} :{1}:", username, matcher.group(1)));
        }
    }

    private void checkBrohoofPattern(List<String> messagesToPost, String message, String username) {
        Pattern pattern = Pattern.compile(brohoofPattern);
        Matcher matcher = pattern.matcher(message);
        if(matcher.matches()) {
            String match = matcher.group(1);
            for (String[] brohoofReply : BROHOOF_REPLIES) {
                if(brohoofReply[0].equals(match)) {
                    messagesToPost.add(MessageFormat.format("{0} {1}", username, brohoofReply[1]));
                    break;
                }
            }
        }
    }

    private void checkTroestPattern(List<String> messagesToPost, String message, String username) {
        Pattern pattern = Pattern.compile(troestPattern);
        Matcher matcher = pattern.matcher(message);
        if(matcher.matches()) {
            messagesToPost.add(MessageFormat.format("{0} :troest:", username, matcher.group(1)));
        }
    }
}
