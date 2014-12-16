package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.UserDTO;
import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class GreetingResponderTest extends ContainerTest {
    private @Inject GreetingResponder greetingResponder;

    @Test
    public void testLoveReplies() {
        simpleTest("signanz :inlove:", "test :inlove:");
        simpleTest("signanz :druegg:", "test :druegg:");
        simpleTest("signanz :knutsch:", "test :knutsch:");
        simpleTest("signanz :hf:", "test :hf:");
        simpleTest("signanz <3", "test <3");
    }

    @Test
    public void testBrohoof() {
        simpleTest("signanz /)", "test (\\");
        simpleTest("signanz (\\", "test /)");
        simpleTest("signanz /]", "test [\\");
        simpleTest("signanz [\\", "test /]");
        simpleTest("signanz (\\ ^ . ^ /)", "test (\\ ^ . ^ /)");
    }

    @Test
    public void testTroest() {
        simpleTest("signanz :traurig:", "test :troest:");
    }

    private void simpleTest(String input, String expectedOutput) {
        MessageDTO messageDTO = getMessageDTO(input);
        ResponderResult responderResult = greetingResponder.processMessage(messageDTO);
        assertResult(responderResult, messageDTO.getMessage(), expectedOutput);
    }

    private void assertResult(ResponderResult responderResult, String expectedMessage, String... expectedMessagesToPost) {
        assertThat(responderResult.getMessage()).isEqualTo(expectedMessage);
        assertThat(responderResult.getMessagesToPost()).hasSize(expectedMessagesToPost.length);

        for(int i = 0; i < expectedMessagesToPost.length; i++) {
            assertThat(responderResult.getMessagesToPost().get(i)).isEqualTo(expectedMessagesToPost[i]);
        }
    }

    private MessageDTO getMessageDTO(String message) {
        MessageDTO messageDTO = new MessageDTO(null, null, null, null, null, false, getTestUser());
        messageDTO.setMessage(message);
        return messageDTO;
    }

    private UserDTO getTestUser() {
        return new UserDTO(0, "test", null);
    }
}
