package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.util.DefaultTimeProvider;
import at.rueckgr.chatbox.util.TimeProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ApplicationScoped
public class TimeProviderService implements TimeProvider {
    private DefaultTimeProvider defaultTimeProvider;

    @PostConstruct
    public void init() {
        defaultTimeProvider = DefaultTimeProvider.getInstance();
    }

    @Override
    public LocalDate currentDate() {
        return defaultTimeProvider.currentDate();
    }

    @Override
    public LocalTime currentTime() {
        return defaultTimeProvider.currentTime();
    }

    @Override
    public LocalDateTime currentDateTime() {
        return defaultTimeProvider.currentDateTime();
    }
}
