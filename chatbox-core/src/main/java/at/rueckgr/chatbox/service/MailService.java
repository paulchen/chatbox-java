package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.util.VelocityService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MailService {
    private @Inject VelocityService velocityService;
    private @Inject StageService stageService;

    public void sendExceptionMail(Exception e) {
        String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);

        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("message", e.getMessage());
        objects.put("stacktrace", stackTrace);
        objects.put("environment", stageService.getEnvironment());

        String messageText = velocityService.renderTemplate("exception", objects);

        sendMail(messageText);
    }

    public void sendUnexpectedMessageCountMail(int expected, int actual, String url) {
        Map<String, Object> objects = new HashMap<String, Object>();
        objects.put("expected", expected);
        objects.put("actual", actual);
        objects.put("url", url);
        objects.put("environment", stageService.getEnvironment());

        String messageText = velocityService.renderTemplate("message_count", objects);

        sendMail(messageText);
    }

    private void sendMail(String messageText) {
        // TODO hard-coded strings
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
