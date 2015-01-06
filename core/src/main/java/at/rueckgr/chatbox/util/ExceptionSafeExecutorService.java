package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.service.MailService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.Callable;

@ApplicationScoped
public class ExceptionSafeExecutorService {
    private @Inject MailService mailService;

    public <T> T execute(Callable<T> callable, T defaultOutput) {
        try {
            return callable.call();
        }
        catch (Throwable e) {
            mailService.sendExceptionMail(e);

            return defaultOutput;
        }
    }
}
