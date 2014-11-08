package at.rueckgr.chatbox.ejb;

import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by paulchen on 08.11.14.
 */
@ApplicationScoped
public class ChatboxTimerDelegate {
    private @Inject Log log;
    private @Inject ChatboxWorker worker;
    private @Inject EntityManager entityManager;

    private boolean running;


    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void init() {
        log.info("Initializing message cache");

        worker.loadExistingShouts();
        invokeWorker();
    }

    public void ensureWorkerRunning() {
        log.info("Routine check if worker is running");

        if (running) {
            return;
        }

        invokeWorker();
    }

    private void invokeWorker() {
        log.info("Worker not running, (re)starting");

        try {
            worker.run();
        }
        finally {
            running = false;
        }
    }
}
