package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.wrapper.exception.PollingException;
import org.apache.commons.logging.Log;

import javax.inject.Inject;
import java.text.MessageFormat;

public class ExceptionHelper {
    private @Inject Log log;

    public void handlePollingException(PollingException e) {
        if (e.getCause() != null) {
            log.error(MessageFormat.format("Exception while obtaining messages: {0}", e.getCause().getMessage()));
        }
        else {
            log.error(MessageFormat.format("Exception while obtaining messages: {0}", e.getMessage()));
        }

        try {
            Thread.sleep(15000L); // TODO magic number
        }
        catch (InterruptedException e1) {
            /* ignore */
        }
    }
}
