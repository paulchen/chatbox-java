package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.ShoutPK;
import at.rueckgr.chatbox.dto.MessageId;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author paulchen
 */
@ApplicationScoped
public class ShoutIdTransformer implements Transformer<ShoutPK, MessageId> {

    @Override
    public MessageId entityToDTO(ShoutPK entity) {
        MessageId messageId = new MessageId();
        updateDTO(messageId, entity);
        return messageId;
    }

    @Override
    public ShoutPK dtoToEntity(MessageId dto) {
        ShoutPK shoutPK = new ShoutPK();
        updateEntity(shoutPK, dto);
        return shoutPK;
    }

    @Override
    public MessageId updateDTO(MessageId dto, ShoutPK entity) {
        dto.setId(entity.getId());
        dto.setEpoch(entity.getEpoch());

        return dto;
    }

    @Override
    public ShoutPK updateEntity(ShoutPK entity, MessageId dto) {
        entity.setId(dto.getId());
        entity.setEpoch(dto.getEpoch());

        return entity;
    }
}
