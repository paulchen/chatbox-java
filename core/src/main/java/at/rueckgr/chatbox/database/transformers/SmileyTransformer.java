package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.dto.SmileyDTO;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author paulchen
 */
@ApplicationScoped
public class SmileyTransformer implements Transformer<Smiley, SmileyDTO> {
    @Override
    public SmileyDTO entityToDTO(Smiley smileyEntity) {
        if(smileyEntity == null) {
            return null;
        }

        return updateDTO(new SmileyDTO(), smileyEntity);
    }

    @Override
    public Smiley dtoToEntity(SmileyDTO smileyDTO) {
        if(smileyDTO == null) {
            return null;
        }

        return updateEntity(new Smiley(), smileyDTO);
    }

    @Override
    public SmileyDTO updateDTO(SmileyDTO smileyDTO, Smiley smileyEntity) {
        smileyDTO.setCode(smileyEntity.getCode());
        smileyDTO.setFilename(smileyEntity.getFilename());
        smileyDTO.setMeaning(smileyEntity.getMeaning());

        return smileyDTO;
    }

    @Override
    public Smiley updateEntity(Smiley smileyEntity, SmileyDTO smileyDTO) {
        smileyEntity.setCode(smileyDTO.getCode());
        smileyEntity.setFilename(smileyDTO.getFilename());
        smileyEntity.setMeaning(smileyDTO.getMeaning());

        return smileyEntity;
    }
}
