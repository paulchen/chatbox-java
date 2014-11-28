package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.service.database.SettingsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class StageService {
    // TODO use enum?
    private static final String ENVIRONMENT_PRODUCTION = "prod";
    private static final String ENVIRONMENT_TEST = "test";
    private static final String ENVIRONMENT_DEVELOPMENT = "dev";

    private @Inject SettingsService settingsService;

    public String getEnvironment() {
        return settingsService.getSetting(Settings.ENVIRONMENT);
    }

    public boolean isProduction() {
        return getEnvironment().equals(ENVIRONMENT_PRODUCTION);
    }

    public boolean isTest() {
        return getEnvironment().equals(ENVIRONMENT_TEST);
    }

    public boolean isDevelopment() {
        return getEnvironment().equals(ENVIRONMENT_DEVELOPMENT);
    }
}
