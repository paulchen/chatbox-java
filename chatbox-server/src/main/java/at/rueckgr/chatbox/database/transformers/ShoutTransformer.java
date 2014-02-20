package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.dto.MessageDTO;

import java.io.Serializable;

public class ShoutTransformer implements Transformer<Shout, MessageDTO>, Serializable {

    private UserTransformer userTransformer = new UserTransformer();

    @Override
    public MessageDTO entityToDTO(Shout entity) {
        if(entity == null) {
            return null;
        }

        // TODO use factory
        return new MessageDTO(entity.getId().getId(), entity.getId().getEpoch(),
                entity.getMessage(), entity.getDate(), entity.getDeleted(),
                userTransformer.entityToDTO(entity.getUserBean()));
    }

    @Override
    public Shout dtoToEntity(MessageDTO dto) {
        if(dto == null) {
            return null;
        }

        return null;
    }
}
