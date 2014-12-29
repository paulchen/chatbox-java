package at.rueckgr.chatbox.signanz.responder;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.UserDTO;

import javax.enterprise.context.ApplicationScoped;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationScoped
public class ResponderTestHelper {
    /**
     * Test a responder with an input which is expected to generate a reply.
     *
     * @param responder {@link ResponderPlugin} to test
     * @param input input message
     * @param expectedOutput reply that is expected to be generated by the plugin
     */
    public void simpleTest(ResponderPlugin responder, String input, String expectedOutput) {
        MessageDTO messageDTO = getMessageDTO(input);
        ResponderResult responderResult = responder.processMessage(messageDTO);
        assertResult(responderResult, messageDTO.getMessage(), expectedOutput);
    }

    /**
     * Test a responder with an input which is expected to not generate a reply.
     *
     * @param responder {@link ResponderPlugin} to test
     * @param input input message
     */
    public void simpleTest(ResponderPlugin responder, String input) {
        MessageDTO messageDTO = getMessageDTO(input);
        ResponderResult responderResult = responder.processMessage(messageDTO);
        assertResult(responderResult, messageDTO.getMessage());
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
