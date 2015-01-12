package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.dailystats.util.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class YearlyStats extends AbstractStatsPlugin {
    private @Inject StatsUtils statsUtils;
    private @Inject TimeService timeService;

    @Override
    public String getQuery() {
        return "SELECT new at.rueckgr.chatbox.signanz.dailystats.util.StatsResult(u, COUNT(u)) " +
                "FROM User u, Shout s " +
                "WHERE s.user = u AND s.deleted = 0 AND s.year = :year " +
                "GROUP BY u " +
                "ORDER BY COUNT(u) DESC, u.name ASC";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        LocalDate yesterday = timeService.currentDate().minusDays(1);
        map.put("year", yesterday.getYear());

        return map;
    }

    @Override
    public boolean isActive() {
        return timeService.currentDate().getDayOfYear() == 1;
    }

    @Override
    public String getDetailsLink() {
        LocalDate yesterday = timeService.currentDate().minusDays(1);

        String year = statsUtils.getPaddedYear(yesterday);

        return MessageFormat.format("year={0}", year);
    }

    @Override
    public String getName() {
        return statsUtils.getPaddedYear(timeService.currentDate().minusDays(1));
    }

    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        List<Class<? extends Plugin>> dependencies = super.getDependencies();
        dependencies.add(MonthlyStats.class);
        return dependencies;
    }
}
