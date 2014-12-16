package at.rueckgr.chatbox.service.database;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class TimeService {
    public Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime fromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public long getEpochSeconds() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }
}
