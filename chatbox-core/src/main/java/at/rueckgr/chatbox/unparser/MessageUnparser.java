package at.rueckgr.chatbox.unparser;

import at.rueckgr.chatbox.unparser.plugins.Unparser;
import at.rueckgr.chatbox.unparser.plugins.UnparserPlugin;
import at.rueckgr.chatbox.util.UnparserUtil;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author paulchen
 */
@ApplicationScoped
public class MessageUnparser {
    private List<Class<? extends UnparserPlugin>> unparsers = new ArrayList<Class<? extends UnparserPlugin>>();

    private @Inject Log log;
    private @Inject UnparserUtil unparserUtil;

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections(this.getClass().getPackage().getName());
        Set<Class<? extends UnparserPlugin>> classes = reflections.getSubTypesOf(UnparserPlugin.class);
        unparsers = resolveDependencies(classes);
    }

    /**
     * Resolves the dependencies between a given set of {@link UnparserPlugin}s. These dependencies are specified using the annotation {@link Unparser}.
     *
     * To resolve these dependencies, a dependency graph is built. The topological ordering of this graph is the result of this method
     *
     * To ensure that the graph is weakly connected, an "artificial" plugin ({@link FirstUnparser}) is introduced which all other plugins depend on. This artificial
     * plugin will not be added to the result list
     *
     * @param classes set of classes
     * @return list of classes in the order keeping their
     * @throws RuntimeException in case the dependency graph contains a cycle
     */
    private List<Class<? extends UnparserPlugin>> resolveDependencies(Set<Class<? extends UnparserPlugin>> classes) {
        DirectedGraph<Class<? extends UnparserPlugin>, DefaultEdge> graph = new DefaultDirectedGraph<Class<? extends UnparserPlugin>, DefaultEdge>(DefaultEdge.class);

        // add artificial plugin
        graph.addVertex(FirstUnparser.class);
        for (Class<? extends UnparserPlugin> clazz : classes) {
            if(clazz.isAnnotationPresent(Unparser.class)) {
                Unparser annotation = clazz.getAnnotation(Unparser.class);

                // add the plugin and its dependency upon the artificial plugin
                graph.addVertex(clazz);
                graph.addEdge(FirstUnparser.class, clazz);

                // add the dependencies which are specified using the annotation
                for (Class<? extends UnparserPlugin> dependency : annotation.dependsOn()) {
                    graph.addEdge(dependency, clazz);
                }
            }
        }

        // ensure that there are no cycles
        CycleDetector<Class<? extends UnparserPlugin>, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        if(cycleDetector.detectCycles()) {
            throw new RuntimeException("The dependencies between the UnparserPlugins contain at least one cycle");
        }

        // determine a topological ordering
        TopologicalOrderIterator<Class<? extends UnparserPlugin>, DefaultEdge> iterator = new TopologicalOrderIterator<Class<? extends UnparserPlugin>, DefaultEdge>(graph);
        List<Class<? extends UnparserPlugin>> result = new ArrayList<Class<? extends UnparserPlugin>>();
        while(iterator.hasNext()) {
            Class<? extends UnparserPlugin> next = iterator.next();

            // exclude the artificial plugin from the result list
            if(!next.equals(FirstUnparser.class)) {
                result.add(next);
            }
        }
        return result;
    }

    public String unparse(String rawMessage) {
        String message = rawMessage;

        for(Class<? extends UnparserPlugin> clazz : unparsers) {
            UnparserPlugin unparserPlugin = BeanProvider.getContextualReference(clazz);
            message = unparserPlugin.unparse(message);
        }

        if(message.contains("<")) {
            log.info(MessageFormat.format("Message contains HTML code after unparsing; message in DB: {0}; unparsed message: {1}", rawMessage, message));

            // remove all remaining HTML
            message = unparserUtil.removeHtml(message);
        }

        return message;
    }

    private class FirstUnparser implements UnparserPlugin {
        @Override
        public String unparse(String input) {
            /* do nothing */
            return null;
        }
    }
}
