package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.unparser.plugins.UnparserPlugin;
import at.rueckgr.chatbox.util.DependencyHelper;
import at.rueckgr.chatbox.util.ExceptionSafeExecutorService;
import at.rueckgr.chatbox.util.UnparserUtil;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author paulchen
 */
@ApplicationScoped
public class MessageUnparser {
    private List<Class<? extends Plugin>> unparsers = new ArrayList<Class<? extends Plugin>>();

    private @Inject Log log;
    private @Inject UnparserUtil unparserUtil;
    private @Inject DependencyHelper dependencyHelper;
    private @Inject ExceptionSafeExecutorService executor;

    @PostConstruct
    public void init() {
        unparsers = dependencyHelper.getPluginTypes(UnparserPlugin.class);
    }

    public String unparse(String rawMessage) {
        String message = rawMessage;

        for (Class<? extends Plugin> clazz : unparsers) {
            final String finalMessage = message;

            UnparserPlugin unparserPlugin = (UnparserPlugin) BeanProvider.getContextualReference(clazz);
            message = executor.execute(() -> unparserPlugin.unparse(finalMessage), message);
        }

        if (message.contains("<")) {
            log.info(MessageFormat.format("Message contains HTML code after unparsing; message in DB: {0}; unparsed message: {1}", rawMessage, message));

            // remove all remaining HTML
            message = unparserUtil.removeHtml(message);
        }

        // TODO improve this
        message = message.replaceAll("&amp;", "&");
        message = message.replaceAll("&lt;", "<");
        message = message.replaceAll("&gt;", ">");
        message = message.replaceAll("&quot;", "\"");
        message = message.replaceAll("&apos;", "'");
        
        return message;
    }
}
