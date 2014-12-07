package at.rueckgr.chatbox.signanz.dailystats;

import at.rueckgr.chatbox.database.model.User;
import lombok.Getter;

@Getter
public class StatsResult {
    private final User user;
    private final long shouts;

    public StatsResult(User user, long shouts) {
        this.user = user;
        this.shouts = shouts;
    }
}
