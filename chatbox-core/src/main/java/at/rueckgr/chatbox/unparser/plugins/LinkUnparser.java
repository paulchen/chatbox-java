package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.regex.Matcher;

@Unparser(dependsOn = IconUnparser.class)
@ApplicationScoped
public class LinkUnparser extends AbstractSearchReplaceUnparser {
    private static final String LINK_PATTERN = "<a href=\"([^\"]*)\" target=\"_blank\">([^<]*)</a>";
    private static final String REPLACEMENT = "[url={0}]{1}[/url]";

    @Override
    protected String getPattern() {
        return LINK_PATTERN;
    }

    @Override
    protected String getReplacement(Matcher matcher) {
        String url = StringEscapeUtils.unescapeHtml4(matcher.group(1));
        String description = StringEscapeUtils.unescapeHtml4(matcher.group(2));

        return MessageFormat.format(REPLACEMENT, url, description);
    }
}
