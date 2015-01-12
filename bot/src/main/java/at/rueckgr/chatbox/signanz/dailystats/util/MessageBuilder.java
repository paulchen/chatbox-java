package at.rueckgr.chatbox.signanz.dailystats.util;

import at.rueckgr.chatbox.Plugin;

public interface MessageBuilder<T extends Plugin, R extends BuilderResult> {
    R buildMessage(T plugin);
}
