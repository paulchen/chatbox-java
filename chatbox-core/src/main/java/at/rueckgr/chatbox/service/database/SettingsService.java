package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.database.model.Settings;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
@Transactional
public class SettingsService {
    private @Inject EntityManager em;

    public String getSetting(Setting setting) {
        Settings settings = em.find(Settings.class, setting.getDatabaseName());
        return (settings == null) ? null : settings.getValue();
    }

    public void saveSetting(Setting setting, String value) {
        String key = setting.getDatabaseName();

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
