package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.dailystats.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class YearlyStats extends AbstractStatsPlugin {
    private @Inject StatsUtils statsUtils;
    private @Inject TimeService timeService;

    @Override
    public String getQuery() {
        return "SELECT new at.rueckgr.chatbox.signanz.dailystats.StatsResult(u, COUNT(u)) " +
                "FROM User u, Shout s " +
                "WHERE s.user = u AND s.deleted = 0 AND s.year = :year " +
                "GROUP BY u " +
                "ORDER BY COUNT(u) DESC, u.name ASC";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        // TODO reset to minusDays(1)
        LocalDate yesterday = LocalDate.now().minusDays(2);
        map.put("year", yesterday.getYear());

        return map;
    }

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
    public String getDetailsLink() {
        // TODO reset to minusDays(1)
        LocalDate yesterday = LocalDate.now().minusDays(2);

        String year = statsUtils.getPaddedYear(yesterday);

        return MessageFormat.format("year={0}", year);
    }

    @Override
    public String getName() {
        // TODO reset to minusDays(1)
        LocalDate yesterday = LocalDate.now().minusDays(2);

        return statsUtils.getPaddedYear(yesterday);
    }

    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        List<Class<? extends Plugin>> dependencies = super.getDependencies();
        dependencies.add(MonthlyStats.class);
        return dependencies;
    }
}
