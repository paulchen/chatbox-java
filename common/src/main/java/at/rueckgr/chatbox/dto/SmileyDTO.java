package at.rueckgr.chatbox.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paulchen
 */
@Data
public class SmileyDTO implements ChatboxDTO {
    private static final long serialVersionUID = 5133055274653686966L;

    private String filename;
    private List<String> codes;
    private String meaning;

    public SmileyDTO() {
        super();
    }

    public SmileyDTO(String filename, String meaning) {
        this.filename = filename;
        this.codes = new ArrayList<String>();
        this.meaning = meaning;
    }
}
