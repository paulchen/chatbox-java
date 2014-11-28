package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.Stage;
import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.*;

public class StageServiceTest extends ContainerTest {
    private @Inject StageService stageService;

    @Test
    public void testService() {
        assertEquals(stageService.getEnvironment(), Stage.DEVELOPMENT);
        assertTrue(stageService.isDevelopment());
        assertFalse(stageService.isTest());
        assertFalse(stageService.isProduction());
    }
}