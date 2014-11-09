package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.User;
import at.rueckgr.chatbox.dto.UserDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
@Transactional
public class UserTransformer implements Transformer<User, UserDTO> {
    private @Inject UserCategoryTransformer userCategoryTransformer;
    private @Inject EntityManager em;

    @Override
    public UserDTO entityToDTO(User userEntity) {
        if(userEntity == null) {
            return null;
        }

        // TODO use factory
        UserDTO userDTO = new UserDTO();
        updateDTO(userDTO, userEntity);
        return userDTO;
    }

    @Override
    public User dtoToEntity(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }

        User userEntity = em.find(User.class, userDTO.getId());
        boolean newEntity = false;
        if(userEntity == null) {
            userEntity = new User();
            newEntity = true;
        }
        updateEntity(userEntity, userDTO);
        if(newEntity) {
            em.persist(userEntity);
        }
        return userEntity;
    }

    @Override
    public void updateDTO(UserDTO userDTO, User userEntity) {
        userDTO.setName(userEntity.getName());
        userDTO.setId(userEntity.getId());
        userDTO.setUserCategory(userCategoryTransformer.entityToDTO(userEntity.getUserCategory()));
    }

    @Override
    public void updateEntity(User userEntity, UserDTO userDTO) {
        userEntity.setName(userDTO.getName());
        userEntity.setId(userDTO.getId());
        userEntity.setUserCategory(userCategoryTransformer.dtoToEntity(userDTO.getUserCategory()));
    }
}
