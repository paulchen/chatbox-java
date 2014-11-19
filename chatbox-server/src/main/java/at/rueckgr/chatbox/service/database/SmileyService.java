package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.Shout;
import at.rueckgr.chatbox.database.model.ShoutSmileys;
import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.database.transformers.SmileyTransformer;
import at.rueckgr.chatbox.dto.SmileyDTO;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
@Transactional
public class SmileyService {
    private @Inject EntityManager em;
    private @Inject SmileyTransformer smileyTransformer;

    public void saveSmiley(SmileyDTO smileyDTO) {
        TypedQuery<Smiley> query = em.createNamedQuery(Smiley.QRY_FIND_BY_FILENAME, Smiley.class);
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

    public void updateSmilies(Shout shoutEntity) {
        Map<Smiley, Integer> shoutSmilies = extractSmileys(shoutEntity);
        TypedQuery<ShoutSmileys> query = em.createNamedQuery(ShoutSmileys.QRY_FIND_BY_SHOUT, ShoutSmileys.class);
        query.setParameter("shout", shoutEntity);
        List<ShoutSmileys> currentSmilies = query.getResultList();

        for(ShoutSmileys smiley : currentSmilies) {
            if(!containsSmiley(shoutSmilies, smiley)) {
                em.remove(smiley);
            }
        }

        // to avoid unique-constraint violations
        em.flush();

        for(Smiley smiley : shoutSmilies.keySet()) {
            if(!containsSmiley(currentSmilies, smiley, shoutSmilies.get(smiley))) {
                ShoutSmileys shoutSmiley = new ShoutSmileys(shoutEntity, smiley, shoutSmilies.get(smiley));
                em.persist(shoutSmiley);
            }
        }
    }

    private boolean containsSmiley(List<ShoutSmileys> currentSmilies, Smiley smiley, Integer count) {
        for (ShoutSmileys currentSmily : currentSmilies) {
            if(currentSmily.getSmiley().equals(smiley) && currentSmily.getCount() == count) {
                return true;
            }
        }

        return false;
    }

    private boolean containsSmiley(Map<Smiley, Integer> shoutSmilies, ShoutSmileys smiley) {
        for (Smiley smiley1 : shoutSmilies.keySet()) {
            if(smiley1.equals(smiley.getSmiley()) && shoutSmilies.get(smiley1) == smiley.getCount()) {
                return true;
            }
        }

        return false;
    }

    private Map<Smiley, Integer> extractSmileys(Shout shoutEntity) {
        final String smileyPattern = "\"/?(pics|images)/([no]b/)?smilies/[^\"]*\\.(gif|png|jpg)";

        String message = shoutEntity.getMessage();
        Pattern pattern = Pattern.compile(smileyPattern);
        Matcher matcher = pattern.matcher(message);

        // TODO does this work correctly?
        Map<Smiley, Integer> smileyList = new HashMap<Smiley, Integer>();
        while (matcher.find()) {
            String smileyFilename = matcher.group(0);
            smileyFilename = smileyFilename.substring(smileyFilename.lastIndexOf('/') + 1);
            Smiley smiley = findByFilenameAndCreate(smileyFilename); // TODO caching
            int count = smileyList.containsKey(smiley) ? smileyList.get(smiley) + 1 : 1;
            smileyList.put(smiley, count);
        }

        return smileyList;
    }

    private Smiley findByFilenameAndCreate(String smileyFilename) {
        Smiley smiley = findByFilename(smileyFilename);
        if(smiley != null) {
            return smiley;
        }
        smiley = new Smiley(smileyFilename);
        em.persist(smiley);
        return smiley;
    }

    public Smiley findByFilename(String smileyFilename) {
        try {
            TypedQuery<Smiley> query = em.createNamedQuery(Smiley.QRY_FIND_BY_FILENAME, Smiley.class);
            query.setParameter("filename", smileyFilename);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
