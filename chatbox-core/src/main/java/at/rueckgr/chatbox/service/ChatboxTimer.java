package at.rueckgr.chatbox.service;

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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Startup
@Singleton
@Lock(LockType.READ)
// @TransactionAttribute is necessary; otherwise, Deltaspike doesn't start its own transaction and the transaction never gets committed
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ChatboxTimer {
    private @Inject Log log;
    private @Inject ChatboxWorker worker;
    private @Inject MailService mailService;

    private @Resource TimerService timerService;

    private volatile boolean running;
    private volatile boolean initialized;

    @PostConstruct
    public void startup() {
        log.info("ChatboxTimer starting up");

        mailService.sendMail();

        running = true;

        timerService.createSingleActionTimer(1000, new TimerConfig());
    }

    @Timeout
    public void init() {
        log.info("Initializing message cache");

        worker.loadExistingShouts();

        initialized = true;

        invokeWorker();
    }

    @Schedule(hour = "*", minute = "*")
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

    public boolean isInitialized() {
        return initialized;
    }
}
