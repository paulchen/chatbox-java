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
        assertThat(stageService.getEnvironment()).isEqualTo(Stage.UNIT_TEST);
        assertThat(stageService.isDevelopment()).isFalse();
        assertThat(stageService.isTest()).isFalse();
        assertThat(stageService.isProduction()).isFalse();
        assertThat(stageService.isUnitTest()).isTrue();
    }
}
