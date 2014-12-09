package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpHelperTest extends ContainerTest {

    private @Inject HttpHelper httpHelper;

    @Test
    public void testFetchChatboxArchiveUrl() throws Exception {
        String url = "https://rueckgr.at/~paulchen/chatbox/";

        String response = httpHelper.fetchChatboxArchiveUrl(url);
        assertThat(response).isNotNull().isNotEmpty();
    }
}
