package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Unparser
@ApplicationScoped
public class IconUnparser extends AbstractUnparserPlugin {
    private static final String ICON_PATTERN = "<a href=\"([^\"]+)\"><img style=\"max-height: 50px\" src=\"([^\"]+)\" /></a>";
    private static final String REPLACEMENT = "[icon]{0}[/icon]";

    @Override
    public String unparse(String input) {
        Pattern pattern = Pattern.compile(ICON_PATTERN);
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer(input.length());

        while(matcher.find()) {
            String match = matcher.group(0);

            String url1 = StringEscapeUtils.unescapeHtml4(matcher.group(1));
            String url2 = StringEscapeUtils.unescapeHtml4(matcher.group(2));

            if(StringUtils.equals(url1, url2)) {
                String replacement = MessageFormat.format(REPLACEMENT, url1);
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(replacement));
            }
            else {
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(match));
            }
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }
}
