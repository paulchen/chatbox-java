package at.rueckgr.chatbox.dto;

import lombok.Data;

/**
 * @author paulchen
 */
@Data
public class SmileyDTO implements ChatboxDTO {
    private static final long serialVersionUID = 5133055274653686966L;

    private String filename;
    private String code;
    private String meaning;

    public SmileyDTO() {
        super();
    }

    public SmileyDTO(String filename, String code, String meaning) {
        this.filename = filename;
        this.code = code;
        this.meaning = meaning;
    }
}
