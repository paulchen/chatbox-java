package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.dailystats.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class WeeklyStats extends AbstractStatsPlugin {
    private @Inject StatsUtils statsUtils;
    private @Inject TimeService timeService;

    @Override
    public String getQuery() {
        return "SELECT new at.rueckgr.chatbox.signanz.dailystats.StatsResult(u, COUNT(u)) " +
                "FROM User u, Shout s " +
                "WHERE s.user = u AND s.deleted = 0 AND s.date >= :start AND s.date < :end " +
                "GROUP BY u " +
                "ORDER BY COUNT(u) DESC, u.name ASC";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        // TODO timezone fuckup
        map.put("start", timeService.toDate(timeService.currentDateTime().minusWeeks(1).truncatedTo(ChronoUnit.DAYS).minusHours(1)));
        map.put("end", timeService.toDate(timeService.currentDateTime().truncatedTo(ChronoUnit.DAYS).minusHours(1)));

        return map;
    }

    @Override
    public boolean isActive() {
        return timeService.currentDate().getDayOfWeek().equals(DayOfWeek.MONDAY);
    }

    @Override
    public String getDetailsLink() {
        return null;
    }

    @Override
    public String getName() {
        return "the last week";
    }

    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        List<Class<? extends Plugin>> dependencies = super.getDependencies();
        dependencies.add(DailyStats.class);
        return dependencies;
    }
}
