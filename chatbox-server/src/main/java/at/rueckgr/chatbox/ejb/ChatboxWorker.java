package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.MessageSorter;
import at.rueckgr.chatbox.dto.SmileyDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import at.rueckgr.chatbox.wrapper.ChatboxWrapperException;
import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ApplicationScoped
public class ChatboxWorker {
    private @Inject Log log;
    private @Inject Chatbox chatbox;
    private @Inject MessageCache messageCache;
    private @Inject WebsocketSessionManager newMessageNotifier;
    private @Inject ShoutTransformer shoutTransformer;
    private @Inject SmileyTransformer smileyTransformer;
    private @Inject MessageUnparser messageUnparser;
    private @Inject DatabaseService databaseService;

    private final class MessageFetchResult {
        private final Set<MessageDTO> newMessages;
        private final Set<MessageDTO> modifiedMessages;
        private final int totalMessagesCount;

        private MessageFetchResult(Set<MessageDTO> newMessages, Set<MessageDTO> modifiedMessages, int totalMessagesCount) {
            this.newMessages = newMessages;
            this.modifiedMessages = modifiedMessages;
            this.totalMessagesCount = totalMessagesCount;
        }

        public Set<MessageDTO> getNewMessages() {
            return newMessages;
        }

        public Set<MessageDTO> getModifiedMessages() {
            return modifiedMessages;
        }

        public int getTotalMessagesCount() {
            return totalMessagesCount;
        }
    }

    private void init() {
        if(!chatbox.hasSession()) {
            String username = databaseService.getSetting(Settings.FORUM_USERNAME);
            String password = databaseService.getSetting(Settings.FORUM_PASSWORD);

            chatbox.setSession(new ChatboxSession(username, password));
        }
    }

    public void loadExistingShouts() {
        init();

        List<MessageDTO> existingShouts = databaseService.getLastShouts(MessageCache.CACHE_SIZE);
        // TODO umgekehrte Reihenfolge notwendig?
        for(int a = existingShouts.size()-1; a>=0; a--) {
            this.messageCache.update(existingShouts.get(a));
        }
    }

    public void run() {
        init();

        log.info("Starting fetcher");

        while (true) {
            log.debug("Fetching chatbox contents...");

            // TODO make try block smaller?
            try {
                MessageFetchResult result = processMessages(chatbox.fetchCurrent(), false);
                if (result.getNewMessages().size() > 0 || result.getModifiedMessages().size() > 0) {
                    newMessageNotifier.newMessages(result.getNewMessages(), result.getModifiedMessages());
                }

                if(result.getNewMessages().size() == result.getTotalMessagesCount()) {
                    int archivePage = 2;
                    while (true) {
                        log.debug(String.format("Fetching chatbox archive page %s", archivePage));

                        result = processMessages(chatbox.fetchArchive(archivePage), true);
                        // TODO notify clients?
                        if(result.getNewMessages().isEmpty()) {
                            break;
                        }
                        archivePage++;
                    }
                }

                // TODO magic value
                databaseService.saveSetting("last_update", String.valueOf(new Date().getTime()/1000));
            }
            catch (ChatboxWrapperException e) {
                log.error("Exception while obtaining messages", e);
            }
        }
    }

    private MessageFetchResult processMessages(List<MessageDTO> messages, boolean checkInDatabase) {
        log.debug(String.format("Fetched %s messages from chatbox", messages.size()));

        Set<MessageDTO> newMessages = new TreeSet<MessageDTO>(new MessageSorter());
        Set<MessageDTO> modifiedMessages = new TreeSet<MessageDTO>(new MessageSorter());
        for (MessageDTO message : messages) {
            message.setMessage(messageUnparser.unparse(message.getRawMessage()));

            MessageCache.MessageStatus status = this.messageCache.update(message);
            if(checkInDatabase && status == MessageCache.MessageStatus.NEW) {
                status = databaseService.getDatabaseStatus(message);
            }
            switch(status) {
                case NEW:
                    databaseService.persistMessage(message);
                    newMessages.add(message);
                    break;

                case MODIFIED:
                    databaseService.updateMessage(message);
                    modifiedMessages.add(message);
                    break;

                case UNMODIFIED:
                default: // fall-through
                            /* nothing to do */
            }
        }

        if (newMessages.size() > 0 || modifiedMessages.size() > 0) {
            if(newMessages.size() > 0) {
                log.info(String.format("%s new message(s)", newMessages.size()));
            }

            if(modifiedMessages.size() > 0) {
                log.info(String.format("%s modified message(s)", modifiedMessages.size()));
            }
        }
        else {
            log.info("No new messages");
        }

        return new MessageFetchResult(newMessages, modifiedMessages, messages.size());
    }

    public void importSmilies() {
        init();

        Date now = new Date();
        // TODO constant
        String setting = databaseService.getSetting("last_smiley_import");
        if(setting != null) {
            long lastImportTimestamp = Long.parseLong(setting);

            // TODO magic number
            if(now.getTime()-lastImportTimestamp < 3600000) {
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

        databaseService.saveSetting("last_smiley_import", String.valueOf(now.getTime()));

        for (SmileyDTO smileyDTO : smilies) {
            databaseService.saveSmiley(smileyDTO);
        }
    }
}
