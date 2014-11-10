package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ApplicationScoped
public class ShoutTransformer implements Transformer<Shout, MessageDTO> {
    private @Inject ShoutIdTransformer shoutIdTransformer;
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

        messageDTO.setMessageId(shoutIdTransformer.entityToDTO(shoutEntity.getId()));
        // messageDTO.setDate(shoutEntity.getDate());
        // TODO fix this ugly fuckup
        messageDTO.setDate(new Date(shoutEntity.getDate().getTime()+3600000));
        messageDTO.setDeleted(shoutEntity.getDeleted() == 1);
        messageDTO.setRawMessage(rawMessage);
        messageDTO.setMessage(message);
        messageDTO.setUser(userTransformer.entityToDTO(shoutEntity.getUser()));

        return messageDTO;
    }

    @Override
    public Shout updateEntity(Shout shoutEntity, MessageDTO messageDTO) {
        shoutEntity.setId(shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        // TODO fix this
        shoutEntity.setPrimaryId(messageDTO.getMessageId().getId());
        // shoutEntity.setDate(messageDTO.getDate());
        // TODO fix this ugly fuckup
        shoutEntity.setDate(new Date(messageDTO.getDate().getTime()-3600000));
        shoutEntity.setDeleted(messageDTO.isDeleted() ? 1 : 0);
        shoutEntity.setMessage(messageDTO.getRawMessage());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(messageDTO.getDate());
        shoutEntity.setYear(calendar.get(Calendar.YEAR));
        shoutEntity.setMonth(calendar.get(Calendar.MONTH) + 1);
        shoutEntity.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        shoutEntity.setHour(calendar.get(Calendar.HOUR_OF_DAY));

        return shoutEntity;
    }
}
