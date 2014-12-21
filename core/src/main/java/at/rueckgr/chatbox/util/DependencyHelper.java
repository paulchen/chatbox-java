package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.AbstractPlugin;
import at.rueckgr.chatbox.Plugin;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DependencyHelper {
    private Reflections reflections;

    @PostConstruct
    public void init() {
        reflections = new Reflections("at.rueckgr.chatbox");
    }

    /**
     * Resolves the dependencies between a given set of {@link Plugin}s. These dependencies are specified using the method {@link Plugin#getDependencies()}.
     *
     * To resolve these dependencies, a dependency graph is built. The topological ordering of this graph is the result of this method
     *
     * To ensure that the graph is weakly connected, an "artificial" plugin ({@link FirstPlugin}) is introduced which all other plugins depend on. This artificial
     * plugin will not be added to the result list
     *
     * @param classes set of classes
     * @return list of classes in the order keeping their
     * @throws RuntimeException in case the dependency graph contains a cycle
     */
    @SuppressWarnings("unchecked")
    private List<Class<? extends Plugin>> resolveDependencies(Collection<Class<? extends Plugin>> classes) {
        DirectedGraph<Class<? extends Plugin>, DefaultEdge> graph = new DefaultDirectedGraph<Class<? extends Plugin>, DefaultEdge>(DefaultEdge.class);

        // add artificial plugin
        graph.addVertex(FirstPlugin.class);

        // TODO OMG, generics hell! refactoring required!
        // we need this list because we can't add an edge between two vertices before these vertices are added
        List<Pair<Class<? extends Plugin>, Class<? extends Plugin>>> edgesToAdd = new ArrayList<Pair<Class<? extends Plugin>, Class<? extends Plugin>>>();

        for (Class<? extends Plugin> clazz : classes) {
            if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            // add the plugin and its dependency upon the artificial plugin
            graph.addVertex(clazz);
            graph.addEdge(FirstPlugin.class, clazz);

            Plugin plugin = BeanProvider.getContextualReference(clazz);

            // add the dependencies which are specified using the annotation
            edgesToAdd.addAll(plugin.getDependencies().stream()
                    .map(dependency -> new ImmutablePair<Class<? extends Plugin>, Class<? extends Plugin>>(dependency, clazz))
                    .collect(Collectors.toList()));
        }

        for (Pair<Class<? extends Plugin>, Class<? extends Plugin>> edge : edgesToAdd) {
            graph.addEdge(edge.getLeft(), edge.getRight());
        }

        // ensure that there are no cycles
        CycleDetector<Class<? extends Plugin>, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        if(cycleDetector.detectCycles()) {
            throw new RuntimeException("The dependencies between the UnparserPlugins contain at least one cycle");
        }

        // determine a topological ordering
        TopologicalOrderIterator<Class<? extends Plugin>, DefaultEdge> iterator = new TopologicalOrderIterator<Class<? extends Plugin>, DefaultEdge>(graph);
        List<Class<? extends Plugin>> result = new ArrayList<Class<? extends Plugin>>();
        while(iterator.hasNext()) {
            Class<? extends Plugin> next = iterator.next();

            // exclude the artificial plugin from the result list
            if(!next.equals(FirstPlugin.class)) {
                result.add(next);
            }
        }
        return result;
    }

    public List<Class<? extends Plugin>> getPluginTypes(final Class<? extends Plugin> pluginSubClass) {
        Set<Class<? extends Plugin>> classes = reflections.getSubTypesOf(Plugin.class);
        return resolveDependencies(Collections2.filter(classes, pluginSubClass::isAssignableFrom));
    }

    private class FirstPlugin extends AbstractPlugin {
    }
}
