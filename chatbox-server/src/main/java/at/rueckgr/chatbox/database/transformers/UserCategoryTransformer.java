package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.UserCategory;
import at.rueckgr.chatbox.dto.UserCategoryDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserCategoryTransformer implements Transformer<UserCategory, UserCategoryDTO> {

    @Override
    public UserCategoryDTO entityToDTO(UserCategory entity) {
        if(entity == null) {
            return null;
        }

        // TODO use factory
        return new UserCategoryDTO(entity.getName(), entity.getColor());
    }

    @Override
    public UserCategory dtoToEntity(UserCategoryDTO dto) {
        if(dto == null) {
            return null;
        }

        // TODO
        return null;
    }
}
