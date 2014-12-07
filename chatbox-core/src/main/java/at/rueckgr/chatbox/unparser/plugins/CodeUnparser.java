package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.regex.Matcher;

@ApplicationScoped
public class CodeUnparser extends AbstractSearchReplaceUnparser {
    private static final String PATTERN = "<!-- BEGIN TEMPLATE: bbcode_code -->\\s*" +
                    "<div class=\"bbcode_container\">\\s*" +
                    "<div class=\"bbcode_description\">Code:</div>\\s*" +
                    "<pre class=\"bbcode_code\" style=\"height:84px;\">" +
                    "([^<]*)" +
                    "</pre>\\s*" +
                    "</div>\\s*" +
                    "<!-- END TEMPLATE: bbcode_code -->";

    private static final String REPLACEMENT = "[code]{0}[/code]";

    @Override
    protected String getPattern() {
        return PATTERN;
    }

    @Override
    protected String getReplacement(Matcher matcher) {
        String match = matcher.group(1);
        return MessageFormat.format(REPLACEMENT, StringEscapeUtils.unescapeHtml4(match));
    }
}
