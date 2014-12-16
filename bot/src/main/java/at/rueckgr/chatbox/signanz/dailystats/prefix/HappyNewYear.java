package at.rueckgr.chatbox.signanz.dailystats.prefix;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class HappyNewYear extends AbstractPrefixPlugin {
    @Override
    public boolean isActive() {
        return LocalDateTime.now().getDayOfYear() == 1;
    }

    @Override
    public String getMessage() {
        return "Happy New Year! :cheer:";
    }
}
