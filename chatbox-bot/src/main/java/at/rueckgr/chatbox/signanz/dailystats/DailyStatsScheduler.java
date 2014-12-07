package at.rueckgr.chatbox.signanz.dailystats;

import at.rueckgr.chatbox.Plugin;
import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.service.database.SettingsService;
import at.rueckgr.chatbox.signanz.BotService;
import at.rueckgr.chatbox.signanz.dailystats.prefix.PrefixPlugin;
import at.rueckgr.chatbox.signanz.dailystats.stats.StatsPlugin;
import at.rueckgr.chatbox.signanz.dailystats.suffix.SuffixPlugin;
import at.rueckgr.chatbox.util.DependencyHelper;
import org.apache.commons.lang3.StringUtils;
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

@Singleton
public class DailyStatsScheduler {
    private @Inject EntityManager em;
    private @Inject BotService botService;
    private @Inject DependencyHelper dependencyHelper;
    private @Inject MailService mailService;
    private @Inject SettingsService settingsService;

    @Schedule // default is midnight
    public void dailyStats() {
        if(!botService.isActive()) {
            return;
        }

        List<String> messages = new ArrayList<String>();
        messages.addAll(buildPrefixMessages());
        messages.addAll(buildStatsMessages());
        messages.addAll(buildSuffixMessages());

        for (String message : messages) {
            try {
                botService.post(message);
            }
            catch (Exception e) {
                mailService.sendExceptionMail(e);
            }
        }

        queryPages();
    }

    private void queryPages() {
        // TODO perform GET request on detail URLs
    }

    private interface MessageBuilder<T extends Plugin> {
        String buildMessage(T plugin);
    }

    @SuppressWarnings("unchecked")
    private <T extends Plugin> List<String> buildMessages(Class<T> pluginSuperClass, MessageBuilder<T> messageBuilder) {
        Collection<Class<? extends Plugin>> pluginTypes = dependencyHelper.getPluginTypes(pluginSuperClass);

        List<String> messages = new ArrayList<String>();
        for (Class<? extends Plugin> clazz : pluginTypes) {
            T prefixClass = (T) BeanProvider.getContextualReference(clazz);

            String message = messageBuilder.buildMessage(prefixClass);
            if(message != null) {
                messages.add(message);
            }
        }

        return messages;
    }

    private List<String> buildPrefixMessages() {
        return buildMessages(PrefixPlugin.class, new MessageBuilder<PrefixPlugin>() {
            @Override
            public String buildMessage(PrefixPlugin plugin) {
                if(plugin.isActive()) {
                    return plugin.getMessage();
                }
                return null;
            }
        });
    }

    private List<String> buildSuffixMessages() {
        return buildMessages(SuffixPlugin.class, new MessageBuilder<SuffixPlugin>() {
            @Override
            public String buildMessage(SuffixPlugin plugin) {
                if(plugin.isActive()) {
                    return plugin.getMessage();
                }
                return null;
            }
        });
    }

    private List<String> buildStatsMessages() {
        return buildMessages(StatsPlugin.class, new MessageBuilder<StatsPlugin>() {
            @Override
            public String buildMessage(StatsPlugin plugin) {
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

    private String buildStatsMessage(List<StatsResult> result, String name, String detailsLink) {
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

        if(detailsLink != null) {
            return MessageFormat.format("Messages in {0}: {1}; top spammers: {2}; [url={3}?{4}]more details[/url]", name, total, topSpammers.toString(), getBaseUrl(), detailsLink);
        }
        return MessageFormat.format("Messages in {0}: {1}; top spammers: {2}", name, total, topSpammers.toString());
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
