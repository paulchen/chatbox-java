package at.rueckgr.chatbox.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DefaultTimeProvider implements TimeProvider {
    private static DefaultTimeProvider instance;

    private DefaultTimeProvider() {
        // private default constructor to avoid instantiation
    }

    public static DefaultTimeProvider getInstance() {
        if (instance == null) {
            instance = new DefaultTimeProvider();
        }

        return instance;
    }

    @Override
    @SuppressWarnings("checkstyle:regexp")
    public LocalDate currentDate() {
        return LocalDate.now();
    }

    @Override
    @SuppressWarnings("checkstyle:regexp")
    public LocalTime currentTime() {
        return LocalTime.now();
    }

    @Override
    @SuppressWarnings("checkstyle:regexp")
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
