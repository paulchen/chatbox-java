package at.rueckgr.chatbox.service;

import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
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
public class UserTimer {
    private @Inject Log log;
    private @Inject ChatboxTimer chatboxTimer;
    private @Inject UserWorker userWorker;

    private @Resource TimerService timerService;

    private volatile boolean running;

    @PostConstruct
    public void startup() {
        log.info("UserWorker starting up");

        startTimer();
    }

    @Timeout
    public void run() {
        if(running) {
            return;
        }

        if(!chatboxTimer.isInitialized()) {
            startTimer();
            return;
        }

        running = true;
        try {
            userWorker.doWork();
        }
        finally {
            startTimer();
            running = false;
        }
    }

    private void startTimer() {
        // TODO wake up regularly to check if timer is running
        // TODO configurable interval
        timerService.createSingleActionTimer(5000, new TimerConfig());
    }
}
