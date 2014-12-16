package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.util.ChatboxUtil;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BotService {
    private final Chatbox chatbox;

    private @Inject SettingsService settingsService;
    private @Inject ChatboxUtil chatboxUtil;
    private @Inject MailService mailService;
    private @Inject Log log;

    public BotService() {
        this.chatbox = new ChatboxImpl();
    }

    @PostConstruct
    public void init() {
        chatboxUtil.init(chatbox);
    }

    public void post(String message) {
        try {
            chatbox.post(message);
        }
        catch (Exception e) {
            log.error("Exception while posting message", e);

            mailService.sendExceptionMail(e);
        }
    }

    public boolean isActive()  {
        return StringUtils.equals(settingsService.getSetting(Setting.BOT_ACTIVE), "1");
    }
}
