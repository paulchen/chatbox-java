package at.rueckgr.chatbox.service;

import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Collection;

@Startup
@Singleton
@Lock(LockType.READ)
// @TransactionAttribute is necessary; otherwise, Deltaspike doesn't start its own transaction and the transaction never gets committed
@TransactionAttribute(TransactionAttributeType.NEVER)
public class UserTimer {
    private @Inject Log log;
    private @Inject ChatboxTimer chatboxTimer;
    private @Inject UserWorker userWorker;
    private @Inject MailService mailService;
    private @Inject StageService stageService;

    private @Resource TimerService timerService;

    private volatile boolean running;
    private volatile boolean unitTest;
    private volatile long timerInterval;

    @PostConstruct
    public void startup() {
        log.info("UserWorker starting up");

        unitTest = stageService.isUnitTest();
        // TODO configurable interval
        timerInterval = 5000L;

//        startTimer();
    }

//    @Schedule(hour = "*", minute = "*")
    public void checkTimerRunning() {
        log.info("Ensuring that timer in UserTimer is running...");

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
        catch (Throwable e) {
            log.error("Exception occurred while recording online users", e);

            mailService.sendExceptionMail(e);
        }
        finally {
            startTimer();
            running = false;
        }
    }

    private void startTimer() {
        // we must not call any service accessing the database here as this may fail in case the database server has been restarted
        // and the connections to the database server have therefore been closed
        if(unitTest) {
            log.info("Current environment is 'unit-test', don't start worker now");
            return;
        }

        synchronized (this) {
            Collection<Timer> timers = timerService.getTimers();
            if(timers.isEmpty()) {
                timerService.createSingleActionTimer(timerInterval, new TimerConfig());
            }
            else if(timers.size() == 1) {
                Timer timer = timers.iterator().next();
                try {
                    timer.getTimeRemaining();
                }
                catch (NoMoreTimeoutsException e) {
                    timerService.createSingleActionTimer(timerInterval, new TimerConfig());
                }
            }
        }
    }
}
