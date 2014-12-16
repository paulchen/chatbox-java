package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class SettingsServiceTest extends ContainerTest {
    private @Inject SettingsService settingsService;

    @Test
    public void testSettings() {
        settingsService.checkSettings();
    }
}
