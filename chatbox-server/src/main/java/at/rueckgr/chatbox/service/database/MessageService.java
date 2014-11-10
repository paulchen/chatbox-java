package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutRevision;
import at.rueckgr.chatbox.database.model.ShoutRevisionPK;
import at.rueckgr.chatbox.database.transformers.ShoutIdTransformer;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.service.MessageCache;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author paulchen
 */
@Transactional
@ApplicationScoped
public class MessageService {
    private @Inject EntityManager em;
    private @Inject ShoutIdTransformer shoutIdTransformer;
    private @Inject ShoutTransformer shoutTransformer;
    private @Inject SmileyService smileyService;
    private @Inject WordService wordService;
    private @Inject UserService userService;

    private ShoutRevision findLatestShoutRevision(Shout shout) {
        if(shout.getId() == null) {
            return null;
        }

        TypedQuery<ShoutRevision> query = em.createNamedQuery(ShoutRevision.QRY_FIND_LATEST, ShoutRevision.class);
        query.setParameter("id", shout.getId().getId());
        query.setParameter("epoch", shout.getId().getEpoch());

        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    private void createShoutRevision(Shout shout) {
        int newRevisionId = 1;
        ShoutRevision shoutRevision = findLatestShoutRevision(shout);
        if(shoutRevision != null) {
            newRevisionId = shoutRevision.getId().getRevision()+1;
        }

        ShoutRevision newRevision = new ShoutRevision();
        newRevision.setId(new ShoutRevisionPK(shout.getId().getId(), shout.getId().getEpoch(), newRevisionId));
        newRevision.setDate(shout.getDate());
        newRevision.setReplaced(new Date(new Date().getTime()-3600000)); // TODO
        newRevision.setText(shout.getMessage());
        newRevision.setUser(shout.getUser().getId());
        // TODO
        newRevision.setPrimaryId(shout.getId().getId());
        newRevision.setShout(shout);

        em.persist(newRevision);
    }

    public void updateMessage(MessageDTO messageDTO) {
        Shout shoutEntity = em.find(Shout.class, shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        createShoutRevision(shoutEntity);

        shoutTransformer.updateEntity(shoutEntity, messageDTO);
        shoutEntity.setUser(userService.findUser(messageDTO.getUser()));

        smileyService.updateSmilies(shoutEntity);
        wordService.updateWords(shoutEntity);
    }

    public void persistMessage(MessageDTO messageDTO) {
        Shout shoutEntity = shoutTransformer.dtoToEntity(messageDTO);
        shoutEntity.setUser(userService.findUser(messageDTO.getUser()));
        em.persist(shoutEntity);

        smileyService.updateSmilies(shoutEntity);
        wordService.updateWords(shoutEntity);
    }

    public MessageCache.MessageStatus getDatabaseStatus(MessageDTO message) {
        Shout shoutEntity = em.find(Shout.class, shoutIdTransformer.dtoToEntity(message.getMessageId()));
        if(shoutEntity == null) {
            return MessageCache.MessageStatus.NEW;
        }
        MessageDTO databaseMessageDTO = shoutTransformer.entityToDTO(shoutEntity);
        if(databaseMessageDTO.equals(message)) {
            return MessageCache.MessageStatus.UNMODIFIED;
        }
        return MessageCache.MessageStatus.MODIFIED;
    }

    public List<MessageDTO> getLastShouts(int maxCount) {
        TypedQuery<Shout> query = em.createNamedQuery(Shout.FIND_LAST, Shout.class);
        query.setMaxResults(maxCount);
        List<MessageDTO> result = new ArrayList<MessageDTO>();
        for(Shout shout : query.getResultList()) {
            result.add(shoutTransformer.entityToDTO(shout));
        }
        return result;
    }

}