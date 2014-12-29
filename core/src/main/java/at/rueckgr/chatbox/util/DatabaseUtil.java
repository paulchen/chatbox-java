package at.rueckgr.chatbox.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class DatabaseUtil {
    private @Inject EntityManager em;

    public void clearCache() {
        em.clear();
    }
}
