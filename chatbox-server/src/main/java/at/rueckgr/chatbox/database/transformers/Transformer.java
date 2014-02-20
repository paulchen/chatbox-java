package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.ChatboxEntity;
import at.rueckgr.chatbox.dto.ChatboxDTO;

public interface Transformer<ENTITY extends ChatboxEntity, DTO extends ChatboxDTO> {
    DTO entityToDTO(ENTITY entity);

    ENTITY dtoToEntity(DTO dto);
}
