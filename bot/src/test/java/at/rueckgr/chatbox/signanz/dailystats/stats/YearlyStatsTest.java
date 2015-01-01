package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static org.testng.Assert.assertEquals;

public class YearlyStatsTest extends ContainerTest {

    private @Inject YearlyStats yearlyStats;

    @Test
    public void testGetName() throws Exception {
        String year = yearlyStats.getName();
        assertEquals(year, String.valueOf(LocalDateTime.now().minusDays(1).getYear()));
    }
}
