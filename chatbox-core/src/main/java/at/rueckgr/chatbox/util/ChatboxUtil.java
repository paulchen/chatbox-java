package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ChatboxUtil {
    private @Inject SettingsService settingsService;

    public void init(Chatbox chatbox) {
        if(!chatbox.hasSession()) {
            String username = settingsService.getSetting(Setting.FORUM_USERNAME);
            String password = settingsService.getSetting(Setting.FORUM_PASSWORD);

            chatbox.setSession(new ChatboxSession(username, password));
        }
    }
}
