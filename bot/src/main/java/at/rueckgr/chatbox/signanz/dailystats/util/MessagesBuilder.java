package at.rueckgr.chatbox.signanz.dailystats.util;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.util.DependencyHelper;
import at.rueckgr.chatbox.util.ExceptionSafeExecutorService;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class MessagesBuilder {
    private @Inject DependencyHelper dependencyHelper;
    private @Inject ExceptionSafeExecutorService exceptionSafeExecutorService;

    @SuppressWarnings("unchecked")
    public <T extends Plugin, R extends BuilderResult> List<R> buildMessages(Class<T> pluginSuperClass, MessageBuilder<T, R> messageBuilder) {
        Collection<Class<? extends Plugin>> pluginTypes = dependencyHelper.getPluginTypes(pluginSuperClass);

        List<R> results = new ArrayList<R>();
        for (Class<? extends Plugin> clazz : pluginTypes) {
            T prefixClass = (T) BeanProvider.getContextualReference(clazz);

            R result = exceptionSafeExecutorService.execute(() -> messageBuilder.buildMessage(prefixClass), null);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }

}
