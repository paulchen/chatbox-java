package at.rueckgr.chatbox.util;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.Serializable;

public class EntityManagerProducer implements Serializable {
    private static final long serialVersionUID = 175800000605821178L;

    @PersistenceUnit(unitName = "chatbox")
    EntityManagerFactory entityManagerFactory;

    @Produces
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close(@Disposes EntityManager entityManager) {
        entityManager.close();
    }
}
