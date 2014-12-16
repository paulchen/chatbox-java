package at.rueckgr.chatbox.signanz.dailystats.suffix;

import at.rueckgr.chatbox.Plugin;

public interface SuffixPlugin extends Plugin {
    boolean isActive();

    String getMessage();
}
