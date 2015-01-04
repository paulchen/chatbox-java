package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.util.TimeProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class TimeService {
    private @Inject TimeProvider timeProvider;

    public Date toDate(LocalDateTime localDateTime) {
        if(localDateTime == null) {
            return null;
        }

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime fromDate(Date date) {
        if(date == null) {
            return null;
        }

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public long getEpochSeconds() {
        return currentDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }

    public Date currentLegacyDate() {
        return toDate(currentDateTime());
    }

    public LocalDate currentDate() {
        return timeProvider.currentDate();
    }

    public LocalTime currentTime() {
        return timeProvider.currentTime();
    }

    public LocalDateTime currentDateTime() {
        return timeProvider.currentDateTime();
    }
}
