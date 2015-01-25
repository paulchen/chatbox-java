package at.rueckgr.chatbox.service;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.ArchivePagesToRefreshDTO;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.SmileyDTO;
import at.rueckgr.chatbox.dto.events.NewMessagesEvent;
import at.rueckgr.chatbox.service.database.ArchivePagesToRefetchService;
import at.rueckgr.chatbox.service.database.MessageService;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.service.database.SmileyService;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.service.database.UserHelper;
import at.rueckgr.chatbox.unparser.MessageUnparser;
import at.rueckgr.chatbox.util.ChatboxUtil;
import at.rueckgr.chatbox.util.DatabaseUtil;
import at.rueckgr.chatbox.util.ExceptionHelper;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxImpl;
import at.rueckgr.chatbox.wrapper.exception.ChatboxWrapperException;
import at.rueckgr.chatbox.wrapper.exception.PollingException;
import at.rueckgr.chatbox.wrapper.exception.WrongMessageCountException;
import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Collection;
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
    private @Inject MailService mailService;
    private @Inject ExceptionHelper exceptionHelper;
    private @Inject StageService stageService;
    private @Inject ChatboxUtil chatboxUtil;
    private @Inject DatabaseUtil databaseUtil;
    private @Inject UserHelper userHelper;
    private @Inject ArchivePagesToRefetchService archivePagesToRefetchService;

    private final Chatbox chatbox;

    public ChatboxWorker() {
        chatbox = new ChatboxImpl();
    }

    private void init() {
        try {
            settingsService.checkSettings();
        }
        catch (Exception e) {
            log.error("Error while checking settings", e);

            mailService.sendExceptionMail(e);
        }

        chatboxUtil.init(chatbox);
    }

    public void loadExistingShouts() {
        init();

        messageService.getLastShouts(MessageCache.CACHE_SIZE).forEach(this.messageCache::update);
    }

    public void run() {
        init();

        log.info("Starting fetcher");

        int errorCount = 0;

        while (true) {
            checkPagesToRefetch();

            log.info("Fetching chatbox contents...");

            databaseUtil.clearCache();

            // TODO make try block smaller?
            try {
                boolean newMessages = false;

                NewMessagesEvent result = processMessages(chatbox.fetchCurrent(), false);

                if (result.getNewMessages().size() > 0) {
                    newMessages = true;
                }
                if (result.getNewMessages().size() > 0 || result.getModifiedMessages().size() > 0) {
                    newMessagesEvent.fire(result);
                }

                if(!stageService.isDevelopment() && result.getNewMessages().size() == result.getTotalMessagesCount()) {
                    int archivePage = 2;
                    while (true) {
                        log.info(MessageFormat.format("Fetching chatbox archive page {0}", archivePage));

                        result = processMessages(chatbox.fetchArchive(archivePage), true);
                        // TODO notify clients?
                        if(result.getNewMessages().isEmpty()) {
                            break;
                        }
                        archivePage++;
                    }
                }

                updateSettings(newMessages);

                errorCount = 0;
                continue;
            }
            catch (PollingException e) {
                exceptionHelper.handlePollingException(e);
            }
            catch (WrongMessageCountException e) {
                log.error("Exception while obtaining messages", e);

                mailService.sendUnexpectedMessageCountMail(e.getExpected(), e.getActual(), e.getUrl());
            }
            catch (Exception e) {
                log.error("Exception while obtaining messages", e);

                mailService.sendExceptionMail(e);
            }

            errorCount++;

            // when the database server has been restarted, the worker fails repeatedly until it is restarted
            if(errorCount > 5) { // TODO magic number
                log.error("Too many errors while fetching chatbox contents, exiting loop now");

                break;
            }
        }
    }

    private void checkPagesToRefetch() {
        List<ArchivePagesToRefreshDTO> pagesToRefetch = archivePagesToRefetchService.getPagesToRefetch();

        pagesToRefetch.stream().forEach((dto) -> {
            log.info(MessageFormat.format("Fetching chatbox archive page {0}", dto.getPage()));

            // TODO duplicate code
            try {
                processMessages(chatbox.fetchArchive(dto.getPage()), true);

                archivePagesToRefetchService.markAsDone(dto);
            }
            catch (WrongMessageCountException e) {
                log.error("Exception while obtaining messages", e);

                mailService.sendUnexpectedMessageCountMail(e.getExpected(), e.getActual(), e.getUrl());
            }
            catch (Exception e) {
                log.error("Exception while obtaining messages", e);

                mailService.sendExceptionMail(e);
            }
        });
    }

    private void updateSettings(boolean newMessages) {
        settingsService.saveSetting(Setting.LAST_UPDATE, String.valueOf(timeService.getEpochSeconds()));

        if(newMessages) {
            settingsService.saveSetting(Setting.MAX_SHOUT_ID, String.valueOf(messageCache.getMaxShoutId()));
            settingsService.saveSetting(Setting.TOTAL_SHOUTS, String.valueOf(messageService.getTotalShouts()));
            settingsService.saveSetting(Setting.VISIBLE_SHOUTS, String.valueOf(messageService.getVisibleShouts()));
        }
    }

    private NewMessagesEvent processMessages(List<MessageDTO> messages, boolean checkInDatabase) {
        userHelper.checkUsers(messages.stream()
                .map(MessageDTO::getUser)
                .distinct());

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
        String setting = settingsService.getSetting(Setting.LAST_SMILEY_IMPORT);
        if(setting != null) {
            long lastImportTimestamp = Long.parseLong(setting);

            // TODO magic number
            if(epochSeconds-lastImportTimestamp < 3600) {
                return;
            }
        }

        log.info("Fetching smiley list");

        Collection<SmileyDTO> smilies;
        try {
            smilies = chatbox.fetchSmilies();
        }
        catch (ChatboxWrapperException e) {
            log.error("Error while fetching smiley list", e);
            return;
        }

        settingsService.saveSetting(Setting.LAST_SMILEY_IMPORT, String.valueOf(epochSeconds));

        smilies.forEach(smileyService::saveSmiley);
    }
}
