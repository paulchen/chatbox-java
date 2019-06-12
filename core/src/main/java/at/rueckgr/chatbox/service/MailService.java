package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.util.VelocityService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.http.StatusLine;
import org.apache.velocity.tools.generic.DateTool;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MailService {
    private @Inject VelocityService velocityService;
    private @Inject StageService stageService;
    private @Inject TimeService timeService;

    private String environment;
    private long exceptionCooldownPeriod;

    private LocalDateTime lastExceptionMail;
    private long unsentExceptionMails;

    @PostConstruct
    public void init() {
        environment = stageService.getEnvironment().getSettingsValue();

        exceptionCooldownPeriod = 300L; // TODO configurable
        unsentExceptionMails = 0;
    }

    public void sendExceptionMail(Throwable e) {
        if (lastExceptionMail != null) {
            long elapsedTime = ChronoUnit.SECONDS.between(lastExceptionMail, timeService.currentDateTime());
            if (elapsedTime < exceptionCooldownPeriod) {
                unsentExceptionMails++;
                return;
            }
        }
        String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);

        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("message", e.getMessage() == null ? "(null)" : e.getMessage());
        objects.put("stacktrace", stackTrace);
        objects.put("environment", environment);
        objects.put("unsentExceptionMails", unsentExceptionMails);
        objects.put("dateTool", new DateTool());
        objects.put("lastExceptionMail", timeService.toDate(lastExceptionMail));

        String messageText = velocityService.renderTemplate("exception", objects);

        sendMail(messageText);

        lastExceptionMail = timeService.currentDateTime();
        unsentExceptionMails = 0;
    }

    public void sendUnexpectedMessageCountMail(int expected, int actual, String url) {
        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("expected", expected);
        objects.put("actual", actual);
        objects.put("url", url);
        objects.put("environment", environment);

        String messageText = velocityService.renderTemplate("message_count", objects);

        sendMail(messageText);
    }

    public void sendHttpRequestFailedMail(String url, StatusLine statusLine) {
        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("url", url);
        objects.put("status_code", statusLine.getStatusCode());
        objects.put("reason", statusLine.getReasonPhrase());
        objects.put("environment", environment);

        String messageText = velocityService.renderTemplate("http_request_failed", objects);

        sendMail(messageText);
    }

    private void sendMail(String messageText) {
        // TODO hard-coded strings; read them from database in the @PostConstruct method
        try {
            Email email = new SimpleEmail();
            email.setHostName("localhost");
            email.setSmtpPort(25);
            email.setFrom("paulchen@rueckgr.at");
            email.setSubject("Chatbox :: Problem");
            email.setMsg(messageText);
            email.addTo("paulchen@rueckgr.at");
            email.send();
        }
        catch (EmailException emailException) {
            throw ExceptionUtils.throwAsRuntimeException(emailException);
        }
    }

}
