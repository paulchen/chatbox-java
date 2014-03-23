package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.Smiley;
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

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;

@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ChatboxWorkerImpl implements ChatboxWorker, Serializable {
    private static final long serialVersionUID = -8912169820328368446L;

    @Inject
    private Log log;

    @Inject
    private Chatbox chatbox;

    @Inject
    private MessageCache messageCache;

    @Inject
    private WebsocketSessionManager newMessageNotifier;

    @Inject
    private EntityManager em;

    @Inject
    private ShoutTransformer shoutTransformer;

    @Inject
    private SmileyTransformer smileyTransformer;

    @Inject
    private MessageUnparser messageUnparser;

    @Inject
    private ChatboxDAO chatboxDAO;

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

    @PostConstruct
    public void init() {
        String username = em.find(Settings.class, Settings.FORUM_USERNAME).getValue();
        String password = em.find(Settings.class, Settings.FORUM_PASSWORD).getValue();

        chatbox.setSession(new ChatboxSession(username, password));

    }

    @Override
    public void loadExistingShouts() {
        // TODO move to ChatboxDAO
        TypedQuery<Shout> query = em.createNamedQuery(Shout.FIND_LAST, Shout.class);
        query.setMaxResults(MessageCache.CACHE_SIZE);
        List<Shout> existingShouts = query.getResultList();

        for(int a = existingShouts.size()-1; a>=0; a--) {
            this.messageCache.update(shoutTransformer.entityToDTO(existingShouts.get(a)));
        }
    }

    @Asynchronous
    @Override
    public Future<String> run() {
        log.info("Starting fetcher");

        while (true) {
            log.debug("Fetching chatbox contents...");

            // TODO make try block smaller?
            try {
                MessageFetchResult result = processMessages(chatbox.fetchCurrent());
                if (result.getNewMessages().size() > 0 || result.getModifiedMessages().size() > 0) {
                    newMessageNotifier.newMessages(result.getNewMessages(), result.getModifiedMessages());
                }

                if(result.getNewMessages().size() == result.getTotalMessagesCount()) {
                    int archivePage = 2;
                    while (true) {
                        log.debug(String.format("Fetching chatbox archive page %s", archivePage));

                        result = processMessages(chatbox.fetchArchive(archivePage));
                        // TODO notify clients?
                        if(result.getNewMessages().isEmpty()) {
                            break;
                        }
                        archivePage++;
                    }
                }
            }
            catch (ChatboxWrapperException e) {
                log.error("Exception while obtaining messages", e);
            }
        }

        // TODO
        // return new AsyncResult<String>("omg");
    }

    private MessageFetchResult processMessages(List<MessageDTO> messages) {
        log.debug(String.format("Fetched %s messages from chatbox", messages.size()));

        Set<MessageDTO> newMessages = new TreeSet<MessageDTO>(new MessageSorter());
        Set<MessageDTO> modifiedMessages = new TreeSet<MessageDTO>(new MessageSorter());
        for (MessageDTO message : messages) {
            message.setMessage(messageUnparser.unparse(message.getRawMessage()));

            switch(this.messageCache.update(message)) {
                case NEW:
                    chatboxDAO.persistMessage(message);
                    newMessages.add(message);
                    break;

                case MODIFIED:
                    chatboxDAO.updateMessage(message);
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

    @Override
    public void importSmilies() {
        Date now = new Date();
        // TODO constant
        Settings setting = em.find(Settings.class, "last_smiley_import");
        if(setting != null) {
            long lastImportTimestamp = Long.parseLong(setting.getValue());

            // TODO magic number
            if(now.getTime()-lastImportTimestamp < 3600000) {
                return;
            }
        }
        else {
            setting = new Settings("last_smiley_import", String.valueOf(now.getTime()));
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

        setting.setValue(String.valueOf(now.getTime()));
        em.persist(setting);

        // TODO place this query into a separate method
        TypedQuery<Smiley> query = em.createNamedQuery(Smiley.FIND_BY_FILENAME, Smiley.class);

        for (SmileyDTO smileyDTO : smilies) {
            query.setParameter("filename", smileyDTO.getFilename());
            try {
                Smiley smiley = query.getSingleResult();
                smileyTransformer.updateEntity(smiley, smileyDTO);
                em.merge(smiley);
            }
            catch (NoResultException e) {
                Smiley smiley = smileyTransformer.dtoToEntity(smileyDTO);
                em.persist(smiley);
                // TODO
            }
        }

        // TODO
    }
}
