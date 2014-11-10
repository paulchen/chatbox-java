package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutPK;
import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.unparser.MessageUnparser;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ApplicationScoped
@Transactional
public class ShoutTransformer implements Transformer<Shout, MessageDTO> {
    private @Inject ShoutIdTransformer shoutIdTransformer;
    private @Inject UserTransformer userTransformer;
    private @Inject MessageUnparser messageUnparser;
    private @Inject EntityManager em;

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
        messageDTO.setDeleted(shoutEntity.getDeleted() == 1);
        messageDTO.setRawMessage(rawMessage);
        messageDTO.setMessage(message);
        messageDTO.setUser(userTransformer.entityToDTO(shoutEntity.getUser()));
    }

    @Override
    public void updateEntity(Shout shoutEntity, MessageDTO messageDTO) {
        shoutEntity.setId(shoutIdTransformer.dtoToEntity(messageDTO.getMessageId()));
        // TODO fix this
        shoutEntity.setPrimaryId(messageDTO.getMessageId().getId());
        // shoutEntity.setDate(messageDTO.getDate());
        // TODO fix this ugly fuckup
        shoutEntity.setDate(new Date(messageDTO.getDate().getTime()-3600000));
        shoutEntity.setDeleted(messageDTO.isDeleted() ? 1 : 0);
        shoutEntity.setMessage(messageDTO.getRawMessage());
        shoutEntity.setUser(userTransformer.dtoToEntity(messageDTO.getUser()));

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(messageDTO.getDate());
        shoutEntity.setYear(calendar.get(Calendar.YEAR));
        shoutEntity.setMonth(calendar.get(Calendar.MONTH) + 1);
        shoutEntity.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        shoutEntity.setHour(calendar.get(Calendar.HOUR_OF_DAY));
    }
}
