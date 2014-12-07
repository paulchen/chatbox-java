package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.AbstractPlugin;
import org.apache.commons.logging.Log;

import javax.inject.Inject;

public abstract class AbstractUnparserPlugin extends AbstractPlugin implements UnparserPlugin {
    protected @Inject Log log;
}
