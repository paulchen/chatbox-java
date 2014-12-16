package at.rueckgr.chatbox.signanz.dailystats;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public class StatsUtils {
    public String getPaddedDay(LocalDate day) {
        return pad(day.getDayOfMonth(), 2);
    }

    public String getPaddedMonth(LocalDate day) {
        return pad(day.getMonthValue(), 2);
    }

    public String getPaddedYear(LocalDate day) {
        return pad(day.getYear(), 4);
    }

    private String pad(int value, int length) {
        return StringUtils.leftPad(String.valueOf(value), length, "0");
    }
}
