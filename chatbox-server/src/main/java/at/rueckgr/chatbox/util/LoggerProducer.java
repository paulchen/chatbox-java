package at.rueckgr.chatbox.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author paulchen
 */
@ApplicationScoped
public class LoggerProducer {
    @Produces
    public Log createLogger(InjectionPoint injectionPoint) {
        String name = injectionPoint.getMember().getDeclaringClass().getName();
        return LogFactory.getLog(name);
    }
}
