package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class ShoutTransformer implements Transformer<Shout, MessageDTO> {
    private @Inject UserTransformer userTransformer;
    private @Inject MessageUnparser messageUnparser;

    @Override
    public MessageDTO entityToDTO(Shout shoutEntity) {
        if(shoutEntity == null) {
            return null;
        }

        return updateDTO(new MessageDTO(), shoutEntity);
    }

    @Override
    public Shout dtoToEntity(MessageDTO messageDTO) {
        if(messageDTO == null) {
            return null;
        }

        return updateEntity(new Shout(), messageDTO);
    }

    @Override
    public MessageDTO updateDTO(MessageDTO messageDTO, Shout shoutEntity) {
        String rawMessage = shoutEntity.getMessage();
        String message = messageUnparser.unparse(rawMessage);

        messageDTO.setPrimaryId(shoutEntity.getPrimaryId());
        messageDTO.setId(shoutEntity.getId());
        messageDTO.setEpoch(shoutEntity.getEpoch());
        // messageDTO.setDate(shoutEntity.getDate());
        // TODO fix this ugly fuckup
        messageDTO.setDate(LocalDateTime.ofInstant(shoutEntity.getDate().toInstant(), ZoneId.systemDefault()).plusHours(1));
        messageDTO.setDeleted(shoutEntity.getDeleted() == 1);
        messageDTO.setRawMessage(rawMessage);
        messageDTO.setMessage(message);
        messageDTO.setUser(userTransformer.entityToDTO(shoutEntity.getUser()));

        return messageDTO;
    }

    @Override
    public Shout updateEntity(Shout shoutEntity, MessageDTO messageDTO) {
        shoutEntity.setPrimaryId(messageDTO.getPrimaryId());
        shoutEntity.setId(messageDTO.getId());
        shoutEntity.setEpoch(messageDTO.getEpoch());
        // shoutEntity.setDate(messageDTO.getDate());
        // TODO fix this ugly fuckup
        shoutEntity.setDate(Date.from(messageDTO.getDate().minusHours(1).atZone(ZoneId.systemDefault()).toInstant()));
        shoutEntity.setDeleted(messageDTO.isDeleted() ? 1 : 0);
        shoutEntity.setMessage(messageDTO.getRawMessage());
        shoutEntity.setYear(messageDTO.getDate().getYear());
        shoutEntity.setMonth(messageDTO.getDate().getMonth().getValue());
        shoutEntity.setDay(messageDTO.getDate().getDayOfMonth());
        shoutEntity.setHour(messageDTO.getDate().getHour());

        return shoutEntity;
    }
}
