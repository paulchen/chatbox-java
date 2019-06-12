package at.rueckgr.chatbox.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class VelocityService {
    private static final String TEMPLATE_PREFIX = "templates/mails/";
    private static final String TEMPLATE_SUFFIX = ".vm";

    private VelocityEngine velocityEngine;

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.put("resource.loader", "class");
        properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine = new VelocityEngine();
        velocityEngine.init(properties);
    }

    public String renderTemplate(String filename) {
        return renderTemplate(filename, null);
    }

    public String renderTemplate(String filename, Map<String, Object> objects) {
        VelocityContext context = new VelocityContext();

        if (objects != null) {
            for (String key : objects.keySet()) {
                context.put(key, objects.get(key));
            }
        }

        Template template = velocityEngine.getTemplate(TEMPLATE_PREFIX + filename + TEMPLATE_SUFFIX);

        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        return stringWriter.toString();
    }
}
