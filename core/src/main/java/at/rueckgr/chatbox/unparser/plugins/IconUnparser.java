package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.regex.Matcher;

@ApplicationScoped
public class IconUnparser extends AbstractSearchReplaceUnparser {
    private static final String ICON_PATTERN = "<a href=\"([^\"]+)\"><img style=\"max-height: 50px\" src=\"([^\"]+)\" /></a>";
    private static final String REPLACEMENT = "[icon]{0}[/icon]";

    @Override
    protected String getPattern() {
        return ICON_PATTERN;
    }

    @Override
    protected String getReplacement(Matcher matcher) {
        String match = matcher.group(0);

        String url1 = StringEscapeUtils.unescapeHtml4(matcher.group(1));
        String url2 = StringEscapeUtils.unescapeHtml4(matcher.group(2));

        if (StringUtils.equals(url1, url2)) {
            return MessageFormat.format(REPLACEMENT, url1);
        }
        else {
             return match;
        }
    }
}
