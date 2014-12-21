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

    @Override
    public ResponderResult processMessage(MessageDTO messageDTO) {
        String message = messageDTO.getMessage();
        String username = messageDTO.getUser().getName();

        List<String> messagesToPost = new ArrayList<String>();
        if(message.trim().equalsIgnoreCase("schl\u00e4gerei")) {
            messagesToPost.add(MessageFormat.format("/me schl\u00e4gt {0}", username));
        }
        return new ResponderResult(message, messagesToPost);
    }
}
