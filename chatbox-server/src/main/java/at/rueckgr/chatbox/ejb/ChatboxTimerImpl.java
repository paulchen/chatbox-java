package at.rueckgr.chatbox.ejb;

import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.concurrent.Future;

@Startup
@Singleton
public class ChatboxTimerImpl implements Serializable, ChatboxTimer {
    private static final long serialVersionUID = -6970243772202015846L;

    @Inject
    private Log log;

    @Inject
    private ChatboxWorker worker;

    private transient Future<String> state;

    @Override
    @PostConstruct
    public void startup() {
        log.info("Starting up");

        worker.loadExistingShouts();

        this.state = worker.run();
    }

    @Override
    @Schedule(hour = "*", minute = "*")
    public void ensureWorkerRunning() {
        log.info("Routine check if worker is running");

        if (this.state != null && this.state.isDone()) {
            log.error("Worker seems to have crashed, restarting");

            this.state = worker.run();
        }
    }
}
