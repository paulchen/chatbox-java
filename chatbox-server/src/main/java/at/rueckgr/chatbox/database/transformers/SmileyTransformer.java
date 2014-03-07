package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.dto.SmileyDTO;

/**
 * @author paulchen
 */
public class SmileyTransformer implements Transformer<Smiley, SmileyDTO> {
    @Override
    public SmileyDTO entityToDTO(Smiley smileyEntity) {
        if(smileyEntity == null) {
            return null;
        }

        SmileyDTO smileyDTO = new SmileyDTO();
        updateDTO(smileyDTO, smileyEntity);
        return smileyDTO;
    }

    @Override
    public Smiley dtoToEntity(SmileyDTO smileyDTO) {
        if(smileyDTO == null) {
            return null;
        }

        Smiley smileyEntity = new Smiley();
        updateEntity(smileyEntity, smileyDTO);
        return smileyEntity;
    }

    @Override
    public void updateDTO(SmileyDTO smileyDTO, Smiley smileyEntity) {
        smileyDTO.setCode(smileyEntity.getCode());
        smileyDTO.setFilename(smileyEntity.getFilename());
        smileyDTO.setMeaning(smileyEntity.getMeaning());
    }

    @Override
    public void updateEntity(Smiley smileyEntity, SmileyDTO smileyDTO) {
        smileyEntity.setCode(smileyDTO.getCode());
        smileyEntity.setFilename(smileyDTO.getFilename());
        smileyEntity.setMeaning(smileyDTO.getMeaning());
    }
}
