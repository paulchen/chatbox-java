package at.rueckgr.chatbox;

import java.util.List;

public interface Plugin {
    List<Class<? extends Plugin>> getDependencies();
}
