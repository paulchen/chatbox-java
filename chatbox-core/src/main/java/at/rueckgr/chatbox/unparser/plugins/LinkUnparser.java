package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Unparser(order = 4)
@ApplicationScoped
public class LinkUnparser extends AbstractUnparserPlugin {
    private static final String ICON_PATTERN = "<a href=\"([^\"]*)\" target=\"_blank\">([^<]*)</a>";
    private static final String REPLACEMENT = "[url={0}]{1}[/url]";

    @Override
    public String unparse(String input) {
        // TODO generate superclass which performs this search-replace-stuff?
        Pattern pattern = Pattern.compile(ICON_PATTERN);
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer(input.length());

        while(matcher.find()) {
            String url = StringEscapeUtils.unescapeHtml4(matcher.group(1));
            String description = StringEscapeUtils.unescapeHtml4(matcher.group(2));

            String replacement = MessageFormat.format(REPLACEMENT, url, description);
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }
}
