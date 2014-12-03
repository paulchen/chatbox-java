package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.Stage;
import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.service.database.SettingsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;

@ApplicationScoped
public class StageService {
    private @Inject SettingsService settingsService;

    public Stage getEnvironment() {
        String stageName = settingsService.getSetting(Settings.ENVIRONMENT);
        for(Stage stage : Stage.values()) {
            if(stage.getSettingsValue().endsWith(stageName)) {
                return stage;
            }
        }

        throw new IllegalStateException(MessageFormat.format("Invalid environment setting in database: {0}", stageName));
    }

    public boolean isProduction() {
        return getEnvironment().equals(Stage.PRODUCTION);
    }

    public boolean isTest() {
        return getEnvironment().equals(Stage.TEST);
    }

    public boolean isDevelopment() {
        return getEnvironment().equals(Stage.DEVELOPMENT);
    }

    public boolean isUnitTest() {
        return getEnvironment().equals(Stage.UNIT_TEST);
    }
}
