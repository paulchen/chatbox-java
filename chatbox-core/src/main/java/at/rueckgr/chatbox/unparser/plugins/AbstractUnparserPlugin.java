package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.logging.Log;

import javax.inject.Inject;

/**
 * @author paulchen
 */
public abstract class AbstractUnparserPlugin implements UnparserPlugin {
    protected @Inject Log log;
}
