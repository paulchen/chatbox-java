package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.SmileyDTO;
import at.rueckgr.chatbox.service.database.MessageService;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.service.database.SmileyService;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.service.events.NewMessagesEvent;
import at.rueckgr.chatbox.unparser.MessageUnparser;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import at.rueckgr.chatbox.wrapper.ChatboxWrapperException;
import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ApplicationScoped
public class ChatboxWorker {
    private @Inject Log log;
    private @Inject MessageCache messageCache;
    private @Inject ShoutTransformer shoutTransformer;
    private @Inject SmileyTransformer smileyTransformer;
    private @Inject MessageUnparser messageUnparser;
    private @Inject MessageService messageService;
    private @Inject SettingsService settingsService;
    private @Inject SmileyService smileyService;
    private @Inject TimeService timeService;
    private @Inject Event<NewMessagesEvent> newMessagesEvent;

    private final Chatbox chatbox;

    public ChatboxWorker() {
        chatbox = new ChatboxImpl();
    }

    private void init() {
        if(!chatbox.hasSession()) {
            String username = settingsService.getSetting(Settings.FORUM_USERNAME);
            String password = settingsService.getSetting(Settings.FORUM_PASSWORD);

            chatbox.setSession(new ChatboxSession(username, password));
        }
    }

    public void loadExistingShouts() {
        init();

        for (MessageDTO existingShout : messageService.getLastShouts(MessageCache.CACHE_SIZE)) {
            this.messageCache.update(existingShout);
        }
    }

    public void run() {
        init();

        log.info("Starting fetcher");

        while (true) {
            log.debug("Fetching chatbox contents...");

            // TODO make try block smaller?
            try {
                NewMessagesEvent result = processMessages(chatbox.fetchCurrent(), false);
                if (result.getNewMessages().size() > 0 || result.getModifiedMessages().size() > 0) {
                    newMessagesEvent.fire(result);
                }

                if(result.getNewMessages().size() == result.getTotalMessagesCount()) {
                    int archivePage = 2;
                    while (true) {
                        log.debug(MessageFormat.format("Fetching chatbox archive page {0}", archivePage));

                        result = processMessages(chatbox.fetchArchive(archivePage), true);
                        // TODO notify clients?
                        if(result.getNewMessages().isEmpty()) {
                            break;
                        }
                        archivePage++;
                    }
                }

                // TODO magic value
                settingsService.saveSetting("last_update", String.valueOf(timeService.getEpochSeconds()));
            }
            catch (ChatboxWrapperException e) {
                log.error("Exception while obtaining messages", e);
            }
        }
    }

    private NewMessagesEvent processMessages(List<MessageDTO> messages, boolean checkInDatabase) {
        log.debug(MessageFormat.format("Fetched {0} messages from chatbox", messages.size()));

        Set<MessageDTO> newMessages = new TreeSet<MessageDTO>();
        Set<MessageDTO> modifiedMessages = new TreeSet<MessageDTO>();
        for (MessageDTO message : messages) {
            message.setMessage(messageUnparser.unparse(message.getRawMessage()));

            MessageCache.MessageStatus status = this.messageCache.update(message);
            if(checkInDatabase && status == MessageCache.MessageStatus.NEW) {
                status = messageService.getDatabaseStatus(message);
            }
            switch(status) {
                case NEW:
                    messageService.persistMessage(message);
                    newMessages.add(message);
                    break;

                case MODIFIED:
                    messageService.updateMessage(message);
                    modifiedMessages.add(message);
                    break;

                case UNMODIFIED:
                default: // fall-through
                            /* nothing to do */
            }

            log.debug("Message successfully processed");
        }

        if (newMessages.size() > 0 || modifiedMessages.size() > 0) {
            if(newMessages.size() > 0) {
                log.info(MessageFormat.format("{0} new message(s)", newMessages.size()));
            }

            if(modifiedMessages.size() > 0) {
                log.info(MessageFormat.format("{0} modified message(s)", modifiedMessages.size()));
            }
        }
        else {
            log.info("No new or modified messages");
        }

        return new NewMessagesEvent(newMessages, modifiedMessages, messages.size());
    }

    public void importSmilies() {
        init();

        long epochSeconds = timeService.getEpochSeconds();
        // TODO constant
        String setting = settingsService.getSetting("last_smiley_import");
        if(setting != null) {
            long lastImportTimestamp = Long.parseLong(setting);

            // TODO magic number
            if(epochSeconds-lastImportTimestamp < 3600) {
                return;
            }
        }

        log.info("Fetching smiley list");

        List<SmileyDTO> smilies;
        try {
            smilies = chatbox.fetchSmilies();
        }
        catch (ChatboxWrapperException e) {
            log.error("Error while fetching smiley list", e);
            return;
        }

        settingsService.saveSetting("last_smiley_import", String.valueOf(epochSeconds));

        for (SmileyDTO smileyDTO : smilies) {
            smileyService.saveSmiley(smileyDTO);
        }
    }
}
