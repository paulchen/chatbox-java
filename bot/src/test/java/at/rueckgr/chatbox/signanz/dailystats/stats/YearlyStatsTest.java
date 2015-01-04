package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.test.ContainerTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

public class YearlyStatsTest extends ContainerTest {

    private @Inject YearlyStats yearlyStats;
    private @Inject TimeService timeService;

    @Test
    public void testGetName() throws Exception {
        String year = yearlyStats.getName();
        assertEquals(year, String.valueOf(timeService.currentDateTime().minusDays(1).getYear()));
    }
}
