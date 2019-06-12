package at.rueckgr.chatbox;

import lombok.Getter;

@Getter
public enum Setting {
    // TODO add type, default value
    FORUM_USERNAME("forum_username", true),
    FORUM_PASSWORD("forum_password", true),
    LAST_UPDATE("last_update", false),
    LAST_SMILEY_IMPORT("last_smiley_import", false),
    ENVIRONMENT("environment", true),
    BOT_ACTIVE("bot_active", true),
    BASE_URL("base_url", true),
    DAILY_STATS_RANKS("daily_stats_ranks", true),
    HOSTNAME("hostname", true),
    PORT("port", true),
    SCHEME("scheme", true),
    UPDATE_USERNAME("update_username", true),
    UPDATE_PASSWORD("update_password", true),
    CURRENT_EPOCH("current_epoch", false),
    MAX_SHOUT_ID("max_shout_id", false),
    TOTAL_SHOUTS("total_shouts", false),
    VISIBLE_SHOUTS("visible_shouts", false);


    private final String databaseName;
    private final boolean required;

    Setting(String databaseName, boolean required) {
        this.databaseName = databaseName;
        this.required = required;
    }
}
