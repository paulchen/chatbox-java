package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.util.VelocityService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.deltaspike.core.util.ExceptionUtils;
import org.apache.http.StatusLine;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MailService {
    private @Inject VelocityService velocityService;
    private @Inject StageService stageService;

    private String environment;

    @PostConstruct
    public void init() {
        environment = stageService.getEnvironment().getSettingsValue();
    }

    public void sendExceptionMail(Throwable e) {
        String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);

        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("message", e.getMessage());
        objects.put("stacktrace", stackTrace);
        objects.put("environment", environment);

        String messageText = velocityService.renderTemplate("exception", objects);

        sendMail(messageText);
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
