package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.database.model.Settings;
import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutSmileys;
import at.rueckgr.chatbox.database.model.ShoutWords;
import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.database.transformers.ShoutIdTransformer;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.SmileyDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author paulchen
 */
@Transactional
@ApplicationScoped
// TODO split this service to MessageService, SettingsService etc.
// TODO use EntityManager only here; remove @Transactional from all other classes
public class DatabaseService {
    private @Inject EntityManager em;
    private @Inject ShoutIdTransformer shoutIdTransformer;
    private @Inject ShoutTransformer shoutTransformer;
    private @Inject SmileyTransformer smileyTransformer;

    public void updateMessage(MessageDTO messageDTO) {
        // TODO this method is a piece of crap
        Shout oldEntity = em.find(Shout.class, shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));

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

        // remove old smileys and words
        List<ShoutSmileys> smileysToRemove = new ArrayList<ShoutSmileys>();
        List<ShoutWords> wordsToRemove = new ArrayList<ShoutWords>();
        for(ShoutSmileys shoutSmiley : oldEntity.getSmilies()) {
            if(!newEntity.getSmilies().contains(shoutSmiley)) {
                smileysToRemove.add(shoutSmiley);
            }
        }
        for(ShoutWords shoutWord : oldEntity.getWords()) {
            if(!newEntity.getWords().contains(shoutWord)) {
                wordsToRemove.add(shoutWord);
            }
        }

        for (ShoutSmileys shoutSmiley : smileysToRemove) {
            em.remove(shoutSmiley);
            oldEntity.getSmilies().remove(shoutSmiley);
        }
        for (ShoutWords shoutWord : wordsToRemove) {
            em.remove(shoutWord);
            oldEntity.getWords().remove(shoutWord);
        }

        // to avoid unique constraint violations when adding an entity that has the same primary key as an entity that has just been removed
        em.flush();

        // persist new smileys and words
        for(ShoutSmileys shoutSmiley : newEntity.getSmilies()) {
            if(!oldEntity.getSmilies().contains(shoutSmiley)) {
                shoutSmiley.setShout(oldEntity);
                em.persist(shoutSmiley);
                oldEntity.getSmilies().add(shoutSmiley);
            }
        }
        for(ShoutWords shoutWord : newEntity.getWords()) {
            if(!oldEntity.getWords().contains(shoutWord)) {
                shoutWord.setShout(oldEntity);
                em.persist(shoutWord);
                oldEntity.getWords().add(shoutWord);
            }
        }
    }

    public void persistMessage(MessageDTO messageDTO) {
        Shout shoutEntity = shoutTransformer.dtoToEntity(messageDTO);
        em.persist(shoutEntity);

        // persist smileys and words
        for(ShoutSmileys shoutSmiley : shoutEntity.getSmilies()) {
            em.persist(shoutSmiley);
        }
        for(ShoutWords shoutWord : shoutEntity.getWords()) {
            em.persist(shoutWord);
        }
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

    public String getSetting(String key) {
        Settings settings = em.find(Settings.class, key);
        return (settings == null) ? null : settings.getValue();
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

    public void saveSetting(String key, String value) {
        Settings settings = em.find(Settings.class, key);
        if(settings != null) {
            settings.setValue(value);
        }
        else {
            settings = new Settings(key, value);
            em.persist(settings);
        }
    }

    public void saveSmiley(SmileyDTO smileyDTO) {
        TypedQuery<Smiley> query = em.createNamedQuery(Smiley.FIND_BY_FILENAME, Smiley.class);
        query.setParameter("filename", smileyDTO.getFilename());

        try {
            Smiley smiley = query.getSingleResult();
            smileyTransformer.updateEntity(smiley, smileyDTO);
        }
        catch (NoResultException e) {
            Smiley smiley = smileyTransformer.dtoToEntity(smileyDTO);
            em.persist(smiley);
        }
    }
}
