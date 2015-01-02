package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.dailystats.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ApplicationScoped
public class MonthlyStats extends AbstractStatsPlugin {
    private @Inject StatsUtils statsUtils;
    private @Inject TimeService timeService;

    @Override
    public String getQuery() {
        return "SELECT new at.rueckgr.chatbox.signanz.dailystats.StatsResult(u, COUNT(u)) " +
                "FROM User u, Shout s " +
                "WHERE s.user = u AND s.deleted = 0 AND s.month = :month AND s.year = :year " +
                "GROUP BY u " +
                "ORDER BY COUNT(u) DESC, u.name ASC";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        map.put("month", yesterday.getMonthValue());
        map.put("year", yesterday.getYear());

        return map;
    }

    @Override
    public boolean isActive() {
        return LocalDate.now().getDayOfMonth() == 1;
    }

    @Override
    public String getDetailsLink() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String month = statsUtils.getPaddedMonth(yesterday);
        String year = statsUtils.getPaddedYear(yesterday);

        return MessageFormat.format("month={0}&year={1}", month, year);
    }

    @Override
    public String getName() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String monthname = yesterday.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return MessageFormat.format("{0} {1,number,#}", monthname, yesterday.getYear());
    }

    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        List<Class<? extends Plugin>> dependencies = super.getDependencies();
        dependencies.add(WeeklyStats.class);
        return dependencies;
    }
}
