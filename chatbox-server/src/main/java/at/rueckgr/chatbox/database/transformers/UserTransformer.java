package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.User;
import at.rueckgr.chatbox.dto.UserDTO;

import java.io.Serializable;

public class UserTransformer implements Transformer<User, UserDTO>, Serializable {

    private UserCategoryTransformer userCategoryTransformer = new UserCategoryTransformer();

    @Override
    public UserDTO entityToDTO(User entity) {
        if(entity == null) {
            return null;
        }

        // TODO use factory
        return new UserDTO(entity.getId(), entity.getName(), userCategoryTransformer.entityToDTO(entity.getUserCategory()));
    }

    @Override
    public User dtoToEntity(UserDTO dto) {
        if(dto == null) {
            return null;
        }

        return null;
    }
}
