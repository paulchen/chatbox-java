package at.rueckgr.chatbox.signanz.dailystats.prefix;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.Month;

@ApplicationScoped
public class HappyNewYear extends AbstractPrefixPlugin {
    @Override
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        // TODO remove this exception for 2015-01-02
        if(now.getYear() == 2015 && now.getMonth() == Month.JANUARY && now.getDayOfMonth() == 2) {
            return true;
        }
        return now.getDayOfYear() == 1;
    }

    @Override
    public String getMessage() {
        return "Happy New Year! :cheer:";
    }
}
