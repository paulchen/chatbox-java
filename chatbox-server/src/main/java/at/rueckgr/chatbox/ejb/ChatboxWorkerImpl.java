package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.dto.MessageCache;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.MessageSorter;
import at.rueckgr.chatbox.wrapper.Chatbox;
import at.rueckgr.chatbox.wrapper.ChatboxSession;
import at.rueckgr.chatbox.wrapper.ChatboxWrapperException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;

@Stateful
public class ChatboxWorkerImpl implements ChatboxWorker, Serializable {
    private static final long serialVersionUID = -8912169820328368446L;

    private Log log = LogFactory.getLog(this.getClass());

    @Inject
    private Chatbox chatbox;

    @Inject
    private MessageCache messageCache;

    @Inject
    private WebsocketSessionManager newMessageNotifier;

    @PostConstruct
    public void init() {
        // TODO
        chatbox.setSession(new ChatboxSession("signanz", "Aif9eeci"));
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
