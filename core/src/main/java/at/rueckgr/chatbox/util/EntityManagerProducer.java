package at.rueckgr.chatbox.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {
    @PersistenceUnit(unitName = "chatbox")
    EntityManagerFactory entityManagerFactory;

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        em.close();
    }
}
