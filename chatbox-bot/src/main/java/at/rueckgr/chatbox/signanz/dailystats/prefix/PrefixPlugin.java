package at.rueckgr.chatbox.signanz.dailystats.prefix;

import at.rueckgr.chatbox.Plugin;

public interface PrefixPlugin extends Plugin {
    boolean isActive();

    String getMessage();
}
