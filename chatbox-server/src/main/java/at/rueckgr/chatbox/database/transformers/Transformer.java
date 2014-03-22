package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.DatabaseThing;
import at.rueckgr.chatbox.dto.DTOThing;

public interface Transformer<ENTITY extends DatabaseThing, DTO extends DTOThing> {
    DTO entityToDTO(ENTITY entity);

    ENTITY dtoToEntity(DTO dto);

    void updateDTO(DTO dto, ENTITY entity);

    void updateEntity(ENTITY entity, DTO dto);
}
