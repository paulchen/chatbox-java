package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.unparser.plugins.Unparser;
import at.rueckgr.chatbox.unparser.plugins.UnparserPlugin;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author paulchen
 */
@ApplicationScoped
public class MessageUnparser {
    private Map<Integer, Class<? extends UnparserPlugin>> unparsers = new HashMap<Integer, Class<? extends UnparserPlugin>>();

    private @Inject Log log;

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName());
        Set<Class<? extends UnparserPlugin>> classes = reflections.getSubTypesOf(UnparserPlugin.class);
        for (Class<? extends UnparserPlugin> clazz : classes) {
            Unparser annotation = clazz.getAnnotation(Unparser.class);
            if(annotation != null) {
                unparsers.put(annotation.order(), clazz);
            }
        }
    }

    public String unparse(String rawMessage) {
        String message = rawMessage;

        for (Map.Entry<Integer, Class<? extends UnparserPlugin>> entry : unparsers.entrySet()) {
            UnparserPlugin unparserPlugin = BeanProvider.getContextualReference(entry.getValue());
            message = unparserPlugin.unparse(message);
        }

        if(message.contains("<")) {
            log.info(MessageFormat.format("Message contains HTML code after unparsing; message in DB: {0}; unparsed message: {1}", rawMessage, message));

            // remove all remaining HTML
            message = message.replaceAll("<[^>]+>", "");
        }

        return message;
    }
}
