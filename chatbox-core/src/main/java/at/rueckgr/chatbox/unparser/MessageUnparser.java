package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.unparser.plugins.UnparserPlugin;
import at.rueckgr.chatbox.util.DependencyHelper;
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

    @PostConstruct
    public void init() {
        unparsers = dependencyHelper.getPluginTypes(UnparserPlugin.class);
    }

    public String unparse(String rawMessage) {
        String message = rawMessage;

        for(Class<? extends Plugin> clazz : unparsers) {
            UnparserPlugin unparserPlugin = (UnparserPlugin) BeanProvider.getContextualReference(clazz);
            message = unparserPlugin.unparse(message);
        }

        if(message.contains("<")) {
            log.info(MessageFormat.format("Message contains HTML code after unparsing; message in DB: {0}; unparsed message: {1}", rawMessage, message));

            // remove all remaining HTML
            message = unparserUtil.removeHtml(message);
        }

        return message;
    }
}
