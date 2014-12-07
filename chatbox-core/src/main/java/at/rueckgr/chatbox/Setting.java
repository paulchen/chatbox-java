package at.rueckgr.chatbox;

import lombok.Getter;

@Getter
public enum Setting {
    FORUM_USERNAME("forum_username", true),
    FORUM_PASSWORD("forum_password", true),
    LAST_UPDATE("last_update", false),
    LAST_SMILEY_IMPORT("last_smiley_import", false),
    ENVIRONMENT("environment", true),
    BOT_ACTIVE("bot_active", true),
    BASE_URL("base_url", true),
    DAILY_STATS_RANKS("daily_stats_ranks", true);

    private final String databaseName;
    private final boolean required;

    private Setting(String databaseName, boolean required) {
        this.databaseName = databaseName;
        this.required = required;
    }
}
