package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.dto.OnlineUsersInfo;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.service.database.UserService;
import at.rueckgr.chatbox.util.ChatboxUtil;
import at.rueckgr.chatbox.util.ExceptionHelper;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import at.rueckgr.chatbox.wrapper.exception.ChatboxWrapperException;
import at.rueckgr.chatbox.wrapper.exception.PollingException;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserWorker {
    private @Inject Log log;
    private @Inject SettingsService settingsService;
    private @Inject UserService userService;
    private @Inject ExceptionHelper exceptionHelper;
    private @Inject ChatboxUtil chatboxUtil;

    private final Chatbox chatbox;

    public UserWorker() {
        chatbox = new ChatboxImpl();
    }

    private void init() {
        chatboxUtil.init(chatbox);
    }

    public void doWork() {
        init();

        try {
            OnlineUsersInfo onlineUsersInfo = chatbox.fetchOnlineUsers();

            userService.logOnlineUsers(onlineUsersInfo);
        }
        catch (PollingException e) {
            exceptionHelper.handlePollingException(e);
        }
        catch (ChatboxWrapperException e) {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }
    }
}
