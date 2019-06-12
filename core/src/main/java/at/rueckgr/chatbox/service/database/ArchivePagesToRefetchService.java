package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.database.model.ArchivePagesToRefetch;
import at.rueckgr.chatbox.database.transformers.ArchivePagesToRefreshTransformer;
import at.rueckgr.chatbox.dto.ArchivePagesToRefreshDTO;
import org.apache.commons.lang3.Validate;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ArchivePagesToRefetchService {
    private @Inject EntityManager em;
    private @Inject ArchivePagesToRefreshTransformer archivePagesToRefreshTransformer;
    private @Inject TimeService timeService;

    public List<ArchivePagesToRefreshDTO> getPagesToRefetch() {
        TypedQuery<ArchivePagesToRefetch> query = em.createNamedQuery(ArchivePagesToRefetch.QRY_FIND_OPEN, ArchivePagesToRefetch.class);

        List<ArchivePagesToRefetch> result = query.getResultList();
        return result.stream().map(archivePagesToRefreshTransformer::entityToDTO).collect(Collectors.toList());
    }

    public void markAsDone(ArchivePagesToRefreshDTO archivePagesToRefreshDTO) {
        Validate.notNull(archivePagesToRefreshDTO);
        Validate.notNull(archivePagesToRefreshDTO.getId());

        ArchivePagesToRefetch archivePagesToRefetch = em.find(ArchivePagesToRefetch.class, archivePagesToRefreshDTO.getId());
        if (archivePagesToRefetch == null) {
            throw new RuntimeException(MessageFormat.format("Database tuple with id {0} not found!", archivePagesToRefreshDTO.getId()));
        }

        archivePagesToRefetch.setDone(timeService.toDate(timeService.currentDateTime()));
    }
}
