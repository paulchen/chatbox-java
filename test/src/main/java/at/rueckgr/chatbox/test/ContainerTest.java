package at.rueckgr.chatbox.test;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.webbeans.config.PropertyLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Qualifier;
import java.util.Properties;

/**
 * taken from http://openwebbeans.apache.org/testing_cdictrl.html
 */
public abstract class ContainerTest {

    protected static volatile CdiContainer cdiContainer;
    // nice to know, since testng executes tests in parallel.
    protected static int containerRefCount;

    protected ProjectStage runInProjectStage() {
        return ProjectStage.UnitTest;
    }

    /**
     * Starts container
     * @throws Exception in case of severe problem
     */
    @BeforeMethod
    public final void beforeMethod() throws Exception {
        containerRefCount++;

        if (cdiContainer == null) {
            // setting up the Apache DeltaSpike ProjectStage
            ProjectStage projectStage = runInProjectStage();
            ProjectStageProducer.setProjectStage(projectStage);

            cdiContainer = CdiContainerLoader.getCdiContainer();

            Properties dbProperties = PropertyLoader.getProperties("db/db.properties");
            cdiContainer.boot(dbProperties);
            cdiContainer.getContextControl().startContexts();
        }
        else {
            cleanInstances();
        }
    }


    public static CdiContainer getCdiContainer() {
        return cdiContainer;
    }

    /**
     * This will fill all the InjectionPoints of the current test class for you
     */
    @BeforeClass
    public final void beforeClass() throws Exception {
        beforeMethod();

        // perform injection into the very own test class
        BeanManager beanManager = cdiContainer.getBeanManager();

        CreationalContext creationalContext = beanManager.createCreationalContext(null);

        AnnotatedType annotatedType = beanManager.createAnnotatedType(this.getClass());
        InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);
        injectionTarget.inject(this, creationalContext);

        // this is a trick we use to have proper DB transactions when using the entitymanager-per-request pattern
        cleanInstances();
        cleanUpDb();
        cleanInstances();
    }

    /**
     * Shuts down container.
     * @throws Exception in case of severe problem
     */
    @AfterMethod
    public final void afterMethod() throws Exception {
        if (cdiContainer != null) {
            cleanInstances();
            containerRefCount--;
        }
    }

    /**
     * clean the NormalScoped contextual instances by stopping and restarting
     * some contexts. You could also restart the ApplicationScoped context
     * if you have some caches in your classes.
     */
    public final void cleanInstances() throws Exception {
        cdiContainer.getContextControl().stopContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(RequestScoped.class);
        cdiContainer.getContextControl().stopContext(SessionScoped.class);
        cdiContainer.getContextControl().startContext(SessionScoped.class);
    }

    @AfterSuite
    public synchronized void shutdownContainer() throws Exception {
        if (cdiContainer != null) {
            cdiContainer.shutdown();
            cdiContainer = null;
        }
    }

    public void finalize() throws Throwable {
        shutdownContainer();
        super.finalize();
    }


    /**
     * Override this method for database clean up.
     *
     * @throws Exception in case of severe problem
     */
    protected void cleanUpDb() throws Exception {
        //Override in subclasses when needed
    }

    protected <T> T getInstance(Class<T> type, Qualifier... qualifiers) {
        return BeanProvider.getContextualReference(type, qualifiers);
    }
}
