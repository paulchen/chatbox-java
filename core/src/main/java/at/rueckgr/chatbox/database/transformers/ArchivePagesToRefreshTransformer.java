package at.rueckgr.chatbox.database.transformers;

import at.rueckgr.chatbox.database.model.ArchivePagesToRefetch;
import at.rueckgr.chatbox.dto.ArchivePagesToRefreshDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArchivePagesToRefreshTransformer implements Transformer<ArchivePagesToRefetch, ArchivePagesToRefreshDTO> {
    @Override
    public ArchivePagesToRefreshDTO entityToDTO(ArchivePagesToRefetch archivePagesToRefetch) {
        return updateDTO(new ArchivePagesToRefreshDTO(), archivePagesToRefetch);
    }

    @Override
    public ArchivePagesToRefetch dtoToEntity(ArchivePagesToRefreshDTO archivePagesToRefreshDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArchivePagesToRefreshDTO updateDTO(ArchivePagesToRefreshDTO archivePagesToRefreshDTO, ArchivePagesToRefetch archivePagesToRefetch) {
        archivePagesToRefreshDTO.setId(archivePagesToRefetch.getId());
        archivePagesToRefreshDTO.setPage(archivePagesToRefetch.getPage());

        return archivePagesToRefreshDTO;
    }

    @Override
    public ArchivePagesToRefetch updateEntity(ArchivePagesToRefetch archivePagesToRefetch, ArchivePagesToRefreshDTO archivePagesToRefreshDTO) {
        throw new UnsupportedOperationException();
    }
}
