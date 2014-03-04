package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;

import javax.inject.Inject;
import java.io.Serializable;

public class ShoutTransformer implements Transformer<Shout, MessageDTO>, Serializable {

    private UserTransformer userTransformer = new UserTransformer();

    @Inject
    private MessageUnparser messageUnparser;

    @Override
    public MessageDTO entityToDTO(Shout entity) {
        if(entity == null) {
            return null;
        }

        String rawMessage = entity.getMessage();
        String message = messageUnparser.unparse(rawMessage);

        // TODO use factory
        return new MessageDTO(entity.getId().getId(), entity.getId().getEpoch(),
                message, entity.getDate(), entity.getDeleted(),
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
