package at.rueckgr.chatbox.signanz.dailystats.stats;

import at.rueckgr.chatbox.Plugin;

import java.util.Map;

public interface StatsPlugin extends Plugin {
    String getQuery();

    Map<String, Object> getParameters();

    boolean isActive();

    String getDetailsLink();

    String getName();
}
