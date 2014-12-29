package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.service.database.SettingsService;
import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SchlaegereiResponder extends AbstractResponderPlugin {
    private @Inject Log log;
    private @Inject SettingsService settingsService;

    private static final String[][] REPLIES = {
            {"(?iu)schl\u00e4gerei!?", "/me schl\u00e4gt {0}" },
            {"(?iu)schlaegerei!?", "/me schlaegt {0}" },

    };

    @Override
    public ResponderResult processMessage(MessageDTO messageDTO) {
        String message = messageDTO.getMessage();
        String username = messageDTO.getUser().getName();

        List<String> messagesToPost = new ArrayList<String>();
        for (String[] reply : REPLIES) {
            if(message.trim().matches(reply[0])) {
                messagesToPost.add(MessageFormat.format(reply[1], username));
            }
        }
        return new ResponderResult(message, messagesToPost);
    }
}
