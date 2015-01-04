package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.Stage;
import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class StageServiceTest extends ContainerTest {
    private @Inject StageService stageService;

    @Test
    public void testService() {
        assertThat(stageService.getEnvironment()).isEqualTo(Stage.DEVELOPMENT);
        assertThat(stageService.isDevelopment()).isTrue();
        assertThat(stageService.isTest()).isFalse();
        assertThat(stageService.isProduction()).isFalse();
        assertThat(stageService.isUnitTest()).isFalse();
    }
}
