package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.User;
import at.rueckgr.chatbox.database.model.UserCategory;
import at.rueckgr.chatbox.database.transformers.UserCategoryTransformer;
import at.rueckgr.chatbox.database.transformers.UserTransformer;
import at.rueckgr.chatbox.dto.UserCategoryDTO;
import at.rueckgr.chatbox.dto.UserDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class UserService {
    private @Inject EntityManager em;
    private @Inject UserTransformer userTransformer;
    private @Inject UserCategoryTransformer userCategoryTransformer;

    public User findUser(UserDTO userDTO) {
        User userEntity = em.find(User.class, userDTO.getId());
        boolean newEntity = false;
        if(userEntity == null) {
            userEntity = new User();
            newEntity = true;
        }
        userTransformer.updateEntity(userEntity, userDTO);
        userEntity.setUserCategory(findUserCategory(userDTO.getUserCategory()));
        if(newEntity) {
            em.persist(userEntity);
        }
        return userEntity;
    }

    private UserCategory findUserCategory(UserCategoryDTO userCategory) {
        UserCategory userCategoryEntity = findCategoryByName(userCategory.getName());
        boolean newEntity = false;
        if(userCategoryEntity == null) {
            userCategoryEntity = new UserCategory();
            newEntity = true;
        }
        userCategoryTransformer.updateEntity(userCategoryEntity, userCategory);
        if(newEntity) {
            em.persist(userCategoryEntity);
        }
        return userCategoryEntity;
    }

    private UserCategory findCategoryByName(String name) {
        TypedQuery<UserCategory> query = em.createNamedQuery(UserCategory.FIND_BY_NAME, UserCategory.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
