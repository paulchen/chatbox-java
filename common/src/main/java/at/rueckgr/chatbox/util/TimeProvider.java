package at.rueckgr.chatbox.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeProvider {
    LocalDate currentDate();

    LocalTime currentTime();

    LocalDateTime currentDateTime();
}
