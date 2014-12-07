package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BotService {
    private final Chatbox chatbox;

    private @Inject SettingsService settingsService;

    public BotService() {
        this.chatbox = new ChatboxImpl();
    }

    @PostConstruct
    public void init() {
        if(!chatbox.hasSession()) {
            String username = settingsService.getSetting(Settings.FORUM_USERNAME);
            String password = settingsService.getSetting(Settings.FORUM_PASSWORD);

            chatbox.setSession(new ChatboxSession(username, password));
        }
    }

    public void post(String message) throws Exception {
        chatbox.post(message);
    }

    public boolean isActive()  {
        return StringUtils.equals(settingsService.getSetting(Settings.BOT_ACTIVE), "1");
    }
}
