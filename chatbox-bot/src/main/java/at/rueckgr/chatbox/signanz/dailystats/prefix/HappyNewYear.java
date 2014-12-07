package at.rueckgr.chatbox.signanz.dailystats.prefix;

import java.time.LocalDateTime;

public class HappyNewYear extends AbstractPrefixPlugin implements PrefixPlugin {
    @Override
    public boolean isActive() {
        return LocalDateTime.now().getDayOfYear() == 1;
    }

    @Override
    public String getMessage() {
        return "Happy New Year! :cheer:";
    }
}
