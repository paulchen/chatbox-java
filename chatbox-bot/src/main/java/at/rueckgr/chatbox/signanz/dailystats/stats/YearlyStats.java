package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.dailystats.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        LocalDate yesterday = LocalDate.now().minusDays(1);
        map.put("year", yesterday.getYear());

        return map;
    }

    @Override
    public boolean isActive() {
        return LocalDate.now().getDayOfYear() == 1;
    }

    @Override
    public String getDetailsLink() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String year = statsUtils.getPaddedYear(yesterday);

        return MessageFormat.format("year={0}", year);
    }

    @Override
    public String getName() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        return MessageFormat.format("{0,number,#}", String.valueOf(yesterday.getYear()));
    }

    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        List<Class<? extends Plugin>> dependencies = super.getDependencies();
        dependencies.add(MonthlyStats.class);
        return dependencies;
    }
}
