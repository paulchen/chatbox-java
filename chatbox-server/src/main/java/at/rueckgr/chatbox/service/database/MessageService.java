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
// TODO use EntityManager only here; remove @Transactional from all other classes
public class MessageService {
    private @Inject EntityManager em;
    private @Inject ShoutIdTransformer shoutIdTransformer;
    private @Inject ShoutTransformer shoutTransformer;
    private @Inject SmileyService smileyService;
    private @Inject WordService wordService;

    private ShoutRevision findLatestShoutRevision(Shout shout) {
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
        // TODO this method is a piece of crap
        Shout oldEntity = em.find(Shout.class, shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        createShoutRevision(oldEntity);

        Shout newEntity = new Shout();
        shoutTransformer.updateEntity(newEntity, messageDTO);

        oldEntity.setDate(newEntity.getDate());
        oldEntity.setDay(newEntity.getDay());
        oldEntity.setDeleted(newEntity.getDeleted());
        oldEntity.setHour(newEntity.getHour());
        oldEntity.setMessage(newEntity.getMessage());
        oldEntity.setMonth(newEntity.getMonth());
        oldEntity.setUser(newEntity.getUser());
        oldEntity.setYear(newEntity.getYear());

        smileyService.updateSmilies(oldEntity);
        wordService.updateWords(oldEntity);
    }

    public void persistMessage(MessageDTO messageDTO) {
        Shout shoutEntity = shoutTransformer.dtoToEntity(messageDTO);
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
