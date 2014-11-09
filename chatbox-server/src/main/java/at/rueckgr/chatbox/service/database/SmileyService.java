package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.SmileyDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class SmileyService {
    private @Inject EntityManager em;
    private @Inject SmileyTransformer smileyTransformer;

    public void saveSmiley(SmileyDTO smileyDTO) {
        TypedQuery<Smiley> query = em.createNamedQuery(Smiley.FIND_BY_FILENAME, Smiley.class);
        query.setParameter("filename", smileyDTO.getFilename());

        try {
            Smiley smiley = query.getSingleResult();
            smileyTransformer.updateEntity(smiley, smileyDTO);
        }
        catch (NoResultException e) {
            Smiley smiley = smileyTransformer.dtoToEntity(smileyDTO);
            em.persist(smiley);
        }
    }
}
