package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.signanz.dailystats.StatsUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class DailyStats extends AbstractStatsPlugin {
    private @Inject StatsUtils statsUtils;

    @Override
    public String getQuery() {
        return "SELECT new at.rueckgr.chatbox.signanz.dailystats.StatsResult(u, COUNT(u)) " +
                "FROM User u, Shout s " +
                "WHERE s.user = u AND s.deleted = 0 AND s.day = :day AND s.month = :month AND s.year = :year " +
                "GROUP BY u " +
                "ORDER BY COUNT(u) DESC, u.name ASC";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<String, Object>();

        LocalDate yesterday = LocalDate.now().minusDays(1);
        map.put("day", yesterday.getDayOfMonth());
        map.put("month", yesterday.getMonthValue());
        map.put("year", yesterday.getYear());

        return map;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getDetailsLink() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String day = statsUtils.getPaddedDay(yesterday);
        String month = statsUtils.getPaddedMonth(yesterday);
        String year = statsUtils.getPaddedYear(yesterday);

        return MessageFormat.format("day={0}&month={1}&year={2}", day, month, year);
    }

    @Override
    public String getName() {
        return "the last 24 hours";
    }
}
