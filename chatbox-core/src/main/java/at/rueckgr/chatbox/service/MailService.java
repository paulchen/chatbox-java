package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.util.VelocityService;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MailService {
    private @Inject VelocityService velocityService;

    public void sendMail() {
        try {
            String messageText = velocityService.renderTemplate("testmail");

            Email email = new SimpleEmail();
            email.setHostName("localhost");
            email.setSmtpPort(25);
            email.setFrom("paulchen@rueckgr.at");
            email.setSubject("TestMail");
            email.setMsg(messageText);
            email.addTo("paulchen@rueckgr.at");
            email.send();
        }
        catch (EmailException e) {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }
    }
}
