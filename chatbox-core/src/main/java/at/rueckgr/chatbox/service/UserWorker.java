package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.dto.OnlineUsersInfo;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.service.database.UserService;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import at.rueckgr.chatbox.wrapper.ChatboxWrapperException;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserWorker {
    private @Inject Log log;
    private @Inject SettingsService settingsService;
    private @Inject UserService userService;

    private final Chatbox chatbox;

    public UserWorker() {
        chatbox = new ChatboxImpl();
    }

    private void init() {
        if(!chatbox.hasSession()) {
            String username = settingsService.getSetting(Settings.FORUM_USERNAME);
            String password = settingsService.getSetting(Settings.FORUM_PASSWORD);

            chatbox.setSession(new ChatboxSession(username, password));
        }
    }

    public void doWork() {
        init();

        try {
            OnlineUsersInfo onlineUsersInfo = chatbox.fetchOnlineUsers();

            userService.logOnlineUsers(onlineUsersInfo);
        }
        catch (ChatboxWrapperException e) {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }
    }
}
