package at.rueckgr.chatbox;

import lombok.Getter;

public enum Stage {
    PRODUCTION("prod"),
    TEST("test"),
    DEVELOPMENT("dev"),
    UNIT_TEST("unit-test");

    @Getter
    private String settingsValue;

    Stage(String settingsValue) {
        this.settingsValue = settingsValue;
    }
}
