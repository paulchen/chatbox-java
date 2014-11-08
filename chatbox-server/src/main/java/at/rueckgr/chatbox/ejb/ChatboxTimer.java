package at.rueckgr.chatbox.ejb;

import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Startup
@Singleton
@Lock(LockType.READ)
public class ChatboxTimer {
    private @Inject Log log;
    private @Inject ChatboxTimerDelegate delegate;

    private @Inject EntityManager em;

    private @Resource TimerService timerService;

    @PostConstruct
    public void startup() {
        log.info("Starting up");

        delegate.setRunning(true);

        timerService.createSingleActionTimer(1000, new TimerConfig());
    }

    @Timeout
    public void init() {
        delegate.init();
    }

    @Schedule(hour = "*", minute = "*")
    public void ensureWorkerRunning() {
        delegate.ensureWorkerRunning();
    }
}
