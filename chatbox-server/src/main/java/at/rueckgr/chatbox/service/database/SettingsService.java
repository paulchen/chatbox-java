package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.Settings;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SettingsService {
    private @Inject EntityManager em;

    public String getSetting(String key) {
        Settings settings = em.find(Settings.class, key);
        return (settings == null) ? null : settings.getValue();
    }

    public void saveSetting(String key, String value) {
        Settings settings = em.find(Settings.class, key);
        if(settings != null) {
            settings.setValue(value);
        }
        else {
            settings = new Settings(key, value);
            em.persist(settings);
        }
    }
}
