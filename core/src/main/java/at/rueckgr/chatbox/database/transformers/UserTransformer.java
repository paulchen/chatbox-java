package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.User;
import at.rueckgr.chatbox.dto.UserDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Transactional
public class UserTransformer implements Transformer<User, UserDTO> {
    private @Inject UserCategoryTransformer userCategoryTransformer;

    @Override
    public UserDTO entityToDTO(User userEntity) {
        if(userEntity == null) {
            return null;
        }

        return updateDTO(new UserDTO(), userEntity);
    }

    @Override
    public User dtoToEntity(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }

        return updateEntity(new User(), userDTO);
    }

    @Override
    public UserDTO updateDTO(UserDTO userDTO, User userEntity) {
        userDTO.setName(userEntity.getName());
        userDTO.setId(userEntity.getId());
        userDTO.setUserCategory(userCategoryTransformer.entityToDTO(userEntity.getUserCategory()));

        return userDTO;
    }

    @Override
    public User updateEntity(User userEntity, UserDTO userDTO) {
        userEntity.setName(userDTO.getName());
        userEntity.setId(userDTO.getId());

        return userEntity;
    }
}
