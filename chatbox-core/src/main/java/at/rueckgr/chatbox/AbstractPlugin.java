package at.rueckgr.chatbox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paulchen
 */
public abstract class AbstractPlugin implements Plugin {
    @Override
    public List<Class<? extends Plugin>> getDependencies() {
        return new ArrayList<Class<? extends Plugin>>();
    }
}
