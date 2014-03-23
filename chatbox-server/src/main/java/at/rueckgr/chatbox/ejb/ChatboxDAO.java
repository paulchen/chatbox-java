package at.rueckgr.chatbox.ejb;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutSmileys;
import at.rueckgr.chatbox.database.model.ShoutWords;
import at.rueckgr.chatbox.database.transformers.ShoutIdTransformer;
import at.rueckgr.chatbox.database.transformers.ShoutTransformer;
import at.rueckgr.chatbox.dto.MessageDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

/**
 * @author paulchen
 */
// TODO interface?
// TODO check uses of @Transactional
@Transactional
@ApplicationScoped
public class ChatboxDAO {
    @Inject
    private EntityManager em;

    @Inject
    private ShoutIdTransformer shoutIdTransformer;

    @Inject
    private ShoutTransformer shoutTransformer;

    @Inject
    private UserTransaction userTransaction;

    @Transactional
    public void updateMessage(MessageDTO messageDTO) {
        Shout oldEntity = em.find(Shout.class, shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        Shout newEntity = shoutTransformer.dtoToEntity(messageDTO);
        em.merge(newEntity);

        // remove old smileys and words
        for(ShoutSmileys shoutSmiley : oldEntity.getSmilies()) {
            if(!newEntity.getSmilies().contains(shoutSmiley)) {
                em.remove(shoutSmiley);
            }
        }
        for(ShoutWords shoutWord : oldEntity.getWords()) {
            if(!newEntity.getWords().contains(shoutWord)) {
                em.remove(shoutWord);
            }
        }

        // persist new smileys and words
        for(ShoutSmileys shoutSmiley : newEntity.getSmilies()) {
            if(!oldEntity.getSmilies().contains(shoutSmiley)) {
                em.persist(shoutSmiley);
            }
        }
        for(ShoutWords shoutWord : newEntity.getWords()) {
            if(!oldEntity.getSmilies().contains(shoutWord)) {
                em.persist(shoutWord);
            }
        }
    }

    @Transactional
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

}
