package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutPK;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;

@ApplicationScoped
public class ShoutTransformer implements Transformer<Shout, MessageDTO>, Serializable {

    @Inject
    private ShoutIdTransformer shoutIdTransformer;

    @Inject
    private UserTransformer userTransformer;

    @Inject
    private MessageUnparser messageUnparser;

    @Inject
    private EntityManager em;

    @Override
    public MessageDTO entityToDTO(Shout shoutEntity) {
        if(shoutEntity == null) {
            return null;
        }

        // TODO use factory
        MessageDTO messageDTO = new MessageDTO();
        updateDTO(messageDTO, shoutEntity);
        return messageDTO;
    }

    @Override
    public Shout dtoToEntity(MessageDTO messageDTO) {
        if(messageDTO == null) {
            return null;
        }

        ShoutPK shoutPK = shoutIdTransformer.dtoToEntity(messageDTO.getMessageId());
        Shout shoutEntity = em.find(Shout.class, shoutPK);
        if(shoutEntity == null) {
            shoutEntity = new Shout();
        }

        updateEntity(shoutEntity, messageDTO);

        return shoutEntity;
    }

    @Override
    public void updateDTO(MessageDTO messageDTO, Shout shoutEntity) {
        String rawMessage = shoutEntity.getMessage();
        String message = messageUnparser.unparse(rawMessage);

        messageDTO.setMessageId(shoutIdTransformer.entityToDTO(shoutEntity.getId()));
        // messageDTO.setDate(shoutEntity.getDate());
        // TODO fix this ugly fuckup
        messageDTO.setDate(new Date(shoutEntity.getDate().getTime()+3600000));
        messageDTO.setDeleted(shoutEntity.getDeleted());
        messageDTO.setRawMessage(rawMessage);
        messageDTO.setMessage(message);
        messageDTO.setUser(userTransformer.entityToDTO(shoutEntity.getUser()));
    }

    @Override
    public void updateEntity(Shout shoutEntity, MessageDTO messageDTO) {
        // TODO create ShoutRevision if anything changes
        // TODO create/update list of words/smilies
        shoutEntity.setId(shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        // shoutEntity.setDate(messageDTO.getDate());
        // TODO fix this ugly fuckup
        shoutEntity.setDate(new Date(messageDTO.getDate().getTime()+3600000));
        shoutEntity.setDeleted(messageDTO.isDeleted());
        shoutEntity.setMessage(messageDTO.getRawMessage());
        shoutEntity.setUser(userTransformer.dtoToEntity(messageDTO.getUser()));
    }
}
