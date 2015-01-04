package at.rueckgr.chatbox.signanz.dailystats.prefix;

import at.rueckgr.chatbox.service.database.TimeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HappyNewYear extends AbstractPrefixPlugin {
    private @Inject TimeService timeService;

    @Override
    public boolean isActive() {
        return timeService.currentDate().getDayOfYear() == 1;
    }

    @Override
    public String getMessage() {
        return "Happy New Year! :cheer:";
    }
}
