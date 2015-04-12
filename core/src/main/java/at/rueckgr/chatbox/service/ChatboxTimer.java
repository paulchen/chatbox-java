package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.database.SettingsService;
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
import java.text.MessageFormat;

@Startup
@Singleton
@Lock(LockType.READ)
// @TransactionAttribute is necessary; otherwise, Deltaspike doesn't start its own transaction and the transaction never gets committed
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ChatboxTimer {
    private @Inject Log log;
    private @Inject ChatboxWorker worker;
    private @Inject SettingsService settingsService;
    private @Inject StageService stageService;

    private @Resource TimerService timerService;

    private volatile boolean running;
    private volatile boolean initialized;

    @PostConstruct
    public void startup() {
        log.info("ChatboxTimer starting up");
        log.info(MessageFormat.format("Environment: {0}", settingsService.getSetting(Setting.ENVIRONMENT)));

        if(stageService.isUnitTest()) {
            unitTesting();
            return;
        }

        startTimer();
    }

    @Timeout
    public void init() {
        if(!initialized) {
            log.info("Initializing message cache");

            worker.loadExistingShouts();

            initialized = true;
        }

        invokeWorker();
    }

    @Schedule(dayOfWeek = "Sun", hour = "5", minute = "0")
//    @Schedule(hour = "*", minute = "*")
    public void ensureWorkerRunning() {
        log.info("Routine check if worker is running");

        if (running) {
            return;
        }

        startTimer();
    }

    private void startTimer() {
        running = true;
        timerService.createSingleActionTimer(1000, new TimerConfig());
    }

    private void invokeWorker() {
        log.info("Worker not running, (re)starting");

        if(stageService.isUnitTest()) {
            unitTesting();
            return;
        }

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

    private void unitTesting() {
        log.info("Current environment is 'unit-test', don't start worker now");
    }
}
