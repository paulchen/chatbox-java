package at.rueckgr.chatbox.signanz.dailystats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.signanz.BotService;
import at.rueckgr.chatbox.signanz.HttpHelper;
import at.rueckgr.chatbox.signanz.dailystats.prefix.PrefixPlugin;
import at.rueckgr.chatbox.signanz.dailystats.stats.StatsPlugin;
import at.rueckgr.chatbox.signanz.dailystats.suffix.SuffixPlugin;
import at.rueckgr.chatbox.util.DependencyHelper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ExceptionUtils;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// TODO refactor this class
@Singleton
public class DailyStatsScheduler {
    private @Inject Log log;
    private @Inject EntityManager em;
    private @Inject BotService botService;
    private @Inject DependencyHelper dependencyHelper;
    private @Inject MailService mailService;
    private @Inject SettingsService settingsService;
    private @Inject HttpHelper httpHelper;

    @Schedule // default is midnight
    public void dailyStats() {
        if(!botService.isActive()) {
            return;
        }

        List<String> messages = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();

        addMessages(messages, buildPrefixMessages());

        List<StatsBuilderResult> builderResults = buildStatsMessages();
        addMessages(messages, builderResults);
        addUrls(urls, builderResults);

        addMessages(messages, buildSuffixMessages());

        for (String message : messages) {
            botService.post(message);
        }

        queryPages(urls);
    }

    private void addMessages(List<String> messages, List<? extends BuilderResult> builderResults) {
        for (BuilderResult builderResult : builderResults) {
            messages.add(builderResult.getResultString());
        }
    }

    private void addUrls(List<String> messages, List<StatsBuilderResult> builderResults) {
        for (StatsBuilderResult builderResult : builderResults) {
            String url = builderResult.getUrl();
            if(url != null) {
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

    private interface MessageBuilder<T extends Plugin, R extends BuilderResult> {
        R buildMessage(T plugin);
    }

    @Getter
    private class BuilderResult {
        private final String resultString;

        public BuilderResult(String resultString) {
            this.resultString = resultString;
        }
    }

    @Getter
    private class StatsBuilderResult extends BuilderResult {
        private final String url;

        public StatsBuilderResult(String resultString, String url) {
            super(resultString);
            this.url = url;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Plugin, R extends BuilderResult> List<R> buildMessages(Class<T> pluginSuperClass, MessageBuilder<T, R> messageBuilder) {
        Collection<Class<? extends Plugin>> pluginTypes = dependencyHelper.getPluginTypes(pluginSuperClass);

        List<R> results = new ArrayList<R>();
        for (Class<? extends Plugin> clazz : pluginTypes) {
            T prefixClass = (T) BeanProvider.getContextualReference(clazz);

            R result = messageBuilder.buildMessage(prefixClass);
            if(result!= null) {
                results.add(result);
            }
        }

        return results;
    }

    private List<BuilderResult> buildPrefixMessages() {
        return buildMessages(PrefixPlugin.class, new MessageBuilder<PrefixPlugin, BuilderResult>() {
            @Override
            public BuilderResult buildMessage(PrefixPlugin plugin) {
                if(plugin.isActive()) {
                    return new BuilderResult(plugin.getMessage());
                }
                return null;
            }
        });
    }

    private List<BuilderResult> buildSuffixMessages() {
        return buildMessages(SuffixPlugin.class, new MessageBuilder<SuffixPlugin, BuilderResult>() {
            @Override
            public BuilderResult buildMessage(SuffixPlugin plugin) {
                if(plugin.isActive()) {
                    return new BuilderResult(plugin.getMessage());
                }
                return null;
            }
        });
    }

    private List<StatsBuilderResult> buildStatsMessages() {
        return buildMessages(StatsPlugin.class, new MessageBuilder<StatsPlugin, StatsBuilderResult>() {
            @Override
            public StatsBuilderResult buildMessage(StatsPlugin plugin) {
                if(!plugin.isActive()) {
                    return null;
                }

                TypedQuery<StatsResult> query = em.createQuery(plugin.getQuery(), StatsResult.class);
                Map<String, Object> parameters = plugin.getParameters();
                for (String key : parameters.keySet()) {
                    query.setParameter(key, parameters.get(key));
                }
                List<StatsResult> result = query.getResultList();

                return buildStatsMessage(result, plugin.getName(), plugin.getDetailsLink());
            }
        });
    }

    private StatsBuilderResult buildStatsMessage(List<StatsResult> result, String name, String detailsLink) {
        long total = 0;
        StringBuilder topSpammers = new StringBuilder();
        int maxRank = getMaxRank();

        for(int rank = 1; rank <= result.size(); rank++) {
            int currentRank = rank;
            total += result.get(rank-1).getShouts();
            if(rank <= maxRank) {
                List<String> usernames = new ArrayList<String>();
                usernames.add(formatUsername(result.get(rank-1)));
                while(rank<result.size() && result.get(rank-1).getShouts() == result.get(rank).getShouts()) {
                    usernames.add(formatUsername(result.get(rank)));
                    total += result.get(rank).getShouts();
                    rank++;
                }

                if(topSpammers.length() > 0) {
                    topSpammers.append(", ");
                }
                topSpammers.append(currentRank).append(". ").append(StringUtils.join(usernames, "/")).append(" (");
                if(usernames.size() > 1) {
                    topSpammers.append("each ");
                }
                topSpammers.append(result.get(rank-1).getShouts()).append(")");
            }
        }

        String message;
        String url = null;
        if(detailsLink != null) {
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
        if(!color.equals("-")) {
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
