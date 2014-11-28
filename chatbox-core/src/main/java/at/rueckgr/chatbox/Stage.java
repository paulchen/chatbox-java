package at.rueckgr.chatbox;

import lombok.Getter;

public enum Stage {
    PRODUCTION("prod"),
    TEST("test"),
    DEVELOPMENT("dev");

    @Getter
    private String settingsValue;

    private Stage(String settingsValue) {
        this.settingsValue = settingsValue;
    }
}
