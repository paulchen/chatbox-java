package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.unparser.plugins.Unparser;
import at.rueckgr.chatbox.unparser.plugins.UnparserPlugin;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author paulchen
 */
public class MessageUnparser implements Serializable {
    private static final long serialVersionUID = 8276059772935603595L;

    private List<UnparserPlugin> unparsers = new ArrayList<UnparserPlugin>();

    @Inject
    private BeanManager beanManager;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName());
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Unparser.class);
        for(Class<?> clazz : annotatedClasses) {
            unparsers.add(createInstance((Class<UnparserPlugin>) clazz));
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends UnparserPlugin> T createInstance(Class<T> clazz) {
        Bean<T> bean = (Bean<T>) beanManager.resolve(beanManager.getBeans(clazz));
        CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);
        return bean.create(creationalContext);
    }

    public String unparse(String rawMessage) {
        String message = rawMessage;

        for(UnparserPlugin unparserPlugin : unparsers) {
            message = unparserPlugin.unparse(message);
        }

        // TODO remove any HTML remaining

        return message;
    }
}
