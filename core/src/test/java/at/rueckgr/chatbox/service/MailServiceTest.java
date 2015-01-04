package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.test.ContainerTest;
import at.rueckgr.chatbox.test.Groups;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class MailServiceTest extends ContainerTest {
    private @Inject MailService mailService;

    @Test(groups = Groups.BROKEN)
    public void testSendExceptionMail() throws Exception {
        mailService.sendExceptionMail(new RuntimeException());
    }

}
