package at.rueckgr.chatbox.dto;

import java.io.Serializable;

/**
 * @author paulchen
 */
public class SmileyDTO implements ChatboxDTO, Serializable {
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "SmileyDTO{"
                + "filename='" + filename + '\''
                + ", code='" + code + '\''
                + ", meaning='" + meaning + '\''
                + '}';
    }
}
