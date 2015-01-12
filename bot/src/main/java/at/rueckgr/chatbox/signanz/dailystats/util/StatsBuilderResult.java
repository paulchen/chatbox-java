package at.rueckgr.chatbox.signanz.dailystats.util;

import lombok.Getter;

@Getter
public class StatsBuilderResult extends BuilderResult {
    private final String url;

    public StatsBuilderResult(String resultString, String url) {
        super(resultString);
        this.url = url;
    }
}
