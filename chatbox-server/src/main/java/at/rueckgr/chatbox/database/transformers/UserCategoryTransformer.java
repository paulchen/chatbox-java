package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.UserCategory;
import at.rueckgr.chatbox.dto.UserCategoryDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCategoryTransformer implements Transformer<UserCategory, UserCategoryDTO> {

    @Override
    public UserCategoryDTO entityToDTO(UserCategory userCategoryEntity) {
        if(userCategoryEntity == null) {
            return null;
        }

        // TODO use factory
        UserCategoryDTO userCategoryDTO = new UserCategoryDTO();
        updateDTO(userCategoryDTO, userCategoryEntity);
        return userCategoryDTO;
    }

    @Override
    public UserCategory dtoToEntity(UserCategoryDTO userCategoryDTO) {
        if(userCategoryDTO == null) {
            return null;
        }

        UserCategory userCategoryEntity = new UserCategory();
        updateEntity(userCategoryEntity, userCategoryDTO);
        return userCategoryEntity;
    }

    @Override
    public void updateDTO(UserCategoryDTO userCategoryDTO, UserCategory userCategoryEntity) {
        userCategoryDTO.setColor(userCategoryEntity.getColor());
        userCategoryDTO.setName(userCategoryEntity.getName());
    }

    @Override
    public void updateEntity(UserCategory userCategoryEntity, UserCategoryDTO userCategoryDTO) {
        userCategoryEntity.setColor(userCategoryDTO.getColor());
        userCategoryEntity.setName(userCategoryDTO.getName());
    }
}
