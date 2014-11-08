package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.UserCategory;
import at.rueckgr.chatbox.dto.UserCategoryDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@ApplicationScoped
@Transactional
public class UserCategoryTransformer implements Transformer<UserCategory, UserCategoryDTO> {
    private @Inject EntityManager em;

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

        UserCategory userCategoryEntity;
        TypedQuery<UserCategory> query = em.createNamedQuery(UserCategory.FIND_BY_NAME, UserCategory.class);
        query.setParameter("name", userCategoryDTO.getName());

        try {
            // query.getResultList();
            userCategoryEntity = query.getSingleResult();
        }
        catch (NoResultException e) {
            userCategoryEntity = new UserCategory();
        }
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
