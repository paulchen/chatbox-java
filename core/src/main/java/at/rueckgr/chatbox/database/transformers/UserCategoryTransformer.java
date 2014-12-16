package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.UserCategory;
import at.rueckgr.chatbox.dto.UserCategoryDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Transactional
public class UserCategoryTransformer implements Transformer<UserCategory, UserCategoryDTO> {

    @Override
    public UserCategoryDTO entityToDTO(UserCategory userCategoryEntity) {
        if(userCategoryEntity == null) {
            return null;
        }

        return updateDTO(new UserCategoryDTO(), userCategoryEntity);
    }

    @Override
    public UserCategory dtoToEntity(UserCategoryDTO userCategoryDTO) {
        if(userCategoryDTO == null) {
            return null;
        }

        return updateEntity(new UserCategory(), userCategoryDTO);
    }

    @Override
    public UserCategoryDTO updateDTO(UserCategoryDTO userCategoryDTO, UserCategory userCategoryEntity) {
        userCategoryDTO.setColor(userCategoryEntity.getColor());
        userCategoryDTO.setName(userCategoryEntity.getName());

        return userCategoryDTO;
    }

    @Override
    public UserCategory updateEntity(UserCategory userCategoryEntity, UserCategoryDTO userCategoryDTO) {
        userCategoryEntity.setColor(userCategoryDTO.getColor());
        userCategoryEntity.setName(userCategoryDTO.getName());

        return userCategoryEntity;
    }
}
