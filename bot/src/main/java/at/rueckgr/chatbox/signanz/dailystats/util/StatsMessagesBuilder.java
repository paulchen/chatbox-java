package at.rueckgr.chatbox.signanz.dailystats.util;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.signanz.dailystats.stats.StatsPlugin;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsMessagesBuilder {
    private @Inject MessagesBuilder messagesBuilder;
    private @Inject EntityManager em;
    private @Inject SettingsService settingsService;
    
    public List<StatsBuilderResult> buildMessages() {
        return messagesBuilder.buildMessages(StatsPlugin.class, plugin -> {
            if (!plugin.isActive()) {
                return null;
            }

            TypedQuery<StatsResult> query = em.createQuery(plugin.getQuery(), StatsResult.class);
            Map<String, Object> parameters = plugin.getParameters();
            for (String key : parameters.keySet()) {
                query.setParameter(key, parameters.get(key));
            }
            List<StatsResult> result = query.getResultList();

            return buildStatsMessage(result, plugin.getName(), plugin.getDetailsLink());
        });
    }

    private StatsBuilderResult buildStatsMessage(List<StatsResult> result, String name, String detailsLink) {
        long total = 0;
        StringBuilder topSpammers = new StringBuilder();
        int maxRank = getMaxRank();

        for (int rank = 1; rank <= result.size(); rank++) {
            int currentRank = rank;
            total += result.get(rank-1).getShouts();
            if (rank <= maxRank) {
                List<String> usernames = new ArrayList<String>();
                usernames.add(formatUsername(result.get(rank-1)));
                while (rank<result.size() && result.get(rank-1).getShouts() == result.get(rank).getShouts()) {
                    usernames.add(formatUsername(result.get(rank)));
                    total += result.get(rank).getShouts();
                    rank++;
                }

                if (topSpammers.length() > 0) {
                    topSpammers.append(", ");
                }
                topSpammers.append(currentRank).append(". ").append(StringUtils.join(usernames, "/")).append(" (");
                if (usernames.size() > 1) {
                    topSpammers.append("each ");
                }
                topSpammers.append(result.get(rank-1).getShouts()).append(")");
            }
        }

        String message;
        String url = null;
        if (detailsLink != null) {
            url = MessageFormat.format("{0}?{1}", getBaseUrl(), detailsLink);
            message = MessageFormat.format("Messages in {0}: {1,number,#}; top spammers: {2}; [url={3}]more details[/url]", name, total, topSpammers.toString(), url);
        }
        else {
            message = MessageFormat.format("Messages in {0}: {1,number,#}; top spammers: {2}", name, total, topSpammers.toString());
        }

        return new StatsBuilderResult(message, url);
    }

    private String formatUsername(StatsResult statsResult) {
        String username = statsResult.getUser().getName();
        String color = statsResult.getUser().getUserCategory().getColor();

        String url;
        try {
            url = MessageFormat.format("{0}?user={1}", getBaseUrl(), URLEncoder.encode(username, "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.throwAsRuntimeException(e);
        }

        String ret = username;
        if (!color.equals("-")) {
            ret = MessageFormat.format("[b][color={0}]{1}[/color][/b]", color, ret);
        }

        return MessageFormat.format("[url={0}]{1}[/url]", url, ret);
    }

    private String getBaseUrl() {
        return settingsService.getSetting(Setting.BASE_URL);
    }

    private int getMaxRank() {
        return Integer.parseInt(settingsService.getSetting(Setting.DAILY_STATS_RANKS));
    }
}
