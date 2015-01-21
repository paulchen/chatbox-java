package at.rueckgr.chatbox.dto;

import lombok.Data;

@Data
public class ArchivePagesToRefreshDTO implements ChatboxDTO {
    private static final long serialVersionUID = -8030510511790658174L;

    private Integer id;
    private Integer page;
}
