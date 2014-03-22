package at.rueckgr.chatbox.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.util.Date;

/**
 * @author paulchen
 */
@Converter
// TODO get rid of this ugly fuckup
public class ShoutDateConverter implements AttributeConverter<Date, Date>, Serializable {

    private static final long serialVersionUID = 13800635423074292L;

    private static final long OFFSET = 3600000;

    @Override
    public Date convertToDatabaseColumn(Date attribute) {
        return new Date(attribute.getTime() - OFFSET);
    }

    @Override
    public Date convertToEntityAttribute(Date dbData) {
        return new Date(dbData.getTime() + OFFSET);
    }
}
