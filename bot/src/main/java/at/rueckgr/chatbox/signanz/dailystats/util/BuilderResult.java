package at.rueckgr.chatbox.signanz.dailystats.util;

import lombok.Getter;

@Getter
public class BuilderResult {
    private final String resultString;

    public BuilderResult(String resultString) {
        this.resultString = resultString;
    }
}
