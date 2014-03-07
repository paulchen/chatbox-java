package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.MessageSorter;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import at.rueckgr.chatbox.wrapper.ChatboxWrapperException;
import org.apache.commons.logging.Log;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;

@Singleton
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
    private EntityManager entityManager;

    @Inject
    private ShoutTransformer messageTransformer;

    @PostConstruct
    public void init() {
        String username = entityManager.find(Settings.class, Settings.FORUM_USERNAME).getValue();
        String password = entityManager.find(Settings.class, Settings.FORUM_PASSWORD).getValue();

        chatbox.setSession(new ChatboxSession(username, password));

        // TODO magic number
        List<Shout> existingShouts = entityManager.createNamedQuery("Shout.findLast", Shout.class).setMaxResults(100).getResultList();
        for(int a = existingShouts.size()-1; a>=0; a--) {
            this.messageCache.add(messageTransformer.entityToDTO(existingShouts.get(a)));
        }
    }

    @Asynchronous
    @Override
    public Future<String> run() {
        log.info("Starting fetcher");

        while (true) {
            log.debug("Fetching chatbox contents...");

            try {
                List<MessageDTO> messages = chatbox.fetchCurrent();

                log.debug(String.format("Fetched %s messages from chatbox", messages.size()));

                // TODO

                Set<MessageDTO> newMessages = new TreeSet<MessageDTO>(new MessageSorter());
                for (MessageDTO message : messages) {
                    // TODO do not call messageCache.contains() twice
                    if (!this.messageCache.contains(message)) {
                        this.messageCache.add(message);

                        newMessages.add(message);
                    }
                }

                if (newMessages.size() > 0) {
                    log.info(String.format("%s new messages", newMessages.size()));

                    // TODO notifications
                    newMessageNotifier.newMessages(newMessages);
                }
                else {
                    log.info("No new messages");
                }
            }
            catch (ChatboxWrapperException e) {
                log.error("Exception while obtaining messages", e);
            }
        }

        // TODO
        // return new AsyncResult<String>("omg");
    }
}
