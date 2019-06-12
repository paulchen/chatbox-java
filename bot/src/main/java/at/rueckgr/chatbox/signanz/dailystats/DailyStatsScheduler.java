package at.rueckgr.chatbox.signanz.dailystats;

import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.service.database.TimeService;
import at.rueckgr.chatbox.signanz.BotService;
import at.rueckgr.chatbox.signanz.HttpHelper;
import at.rueckgr.chatbox.signanz.dailystats.util.BuilderResult;
import at.rueckgr.chatbox.signanz.dailystats.util.PrefixMessagesBuilder;
import at.rueckgr.chatbox.signanz.dailystats.util.StatsBuilderResult;
import at.rueckgr.chatbox.signanz.dailystats.util.StatsMessagesBuilder;
import at.rueckgr.chatbox.signanz.dailystats.util.SuffixMessagesBuilder;
import org.apache.commons.logging.Log;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class DailyStatsScheduler {
    private @Inject Log log;
    private @Inject BotService botService;
    private @Inject MailService mailService;
    private @Inject HttpHelper httpHelper;
    private @Inject TimeService timeService;
    private @Inject PrefixMessagesBuilder prefixMessagesBuilder;
    private @Inject SuffixMessagesBuilder suffixMessagesBuilder;
    private @Inject StatsMessagesBuilder statsMessagesBuilder;

    @Schedule // default is midnight
    public void dailyStats() {
        if (!botService.isActive()) {
            return;
        }
        if (!isMidnight()) {
            // I don't know why this is necessary; @Schedule seems to fire on deployment?!
            return;
        }

        List<String> messages = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();

        addMessages(messages, prefixMessagesBuilder.buildMessages());

        List<StatsBuilderResult> builderResults = statsMessagesBuilder.buildMessages();
        addMessages(messages, builderResults);
        addUrls(urls, builderResults);

        addMessages(messages, suffixMessagesBuilder.buildMessages());

        messages.forEach(botService::post);

        queryPages(urls);
    }

    private boolean isMidnight() {
        LocalTime localTime = timeService.currentTime();

        return localTime.getHour() == 0 && localTime.getMinute() == 0;
    }

    private void addMessages(List<String> messages, List<? extends BuilderResult> builderResults) {
        messages.addAll(builderResults.stream()
                .map(BuilderResult::getResultString)
                .collect(Collectors.toList()));
    }

    private void addUrls(List<String> messages, List<StatsBuilderResult> builderResults) {
        for (StatsBuilderResult builderResult : builderResults) {
            String url = builderResult.getUrl();
            if (url != null) {
                messages.add(url);
            }
        }
    }

    private void queryPages(List<String> urls) {
        for (String url : urls) {
            try {
                httpHelper.fetchChatboxArchiveUrl(url);
            }
            catch (Exception e) {
                log.error(MessageFormat.format("Error while retrieving URL {0}", url), e);

                mailService.sendExceptionMail(e);
            }
        }
    }
}
