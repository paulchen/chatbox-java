package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.util.UnparserUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Unparser
@ApplicationScoped
public class PHPUnparser extends AbstractSearchReplaceUnparser {
    private static final String PATTERN = "<!-- BEGIN TEMPLATE: bbcode_php -->\\s*" +
                    "<div class=\"bbcode_container\">\\s*" +
                    "<div class=\"bbcode_description\">PHP Code:</div>\\s*" +
                    "<div class=\"bbcode_code\" style=\"height:84px;\"><code><code>" +
                    "(.*?)" + // use non-greedy ? here because the rendered PHP code contains lots of HTML tags so we cannot use [^<]*
                    "</code></code></div>\\s*" +
                    "</div>\\s*" +
                    "<!-- END TEMPLATE: bbcode_php -->";

    private static final String REPLACEMENT = "[php]{0}[/php]";

    private @Inject UnparserUtil unparserUtil;

    @Override
    protected String getPattern() {
        return PATTERN;
    }

    @Override
    protected String getReplacement(Matcher matcher) {
        String match = matcher.group(1);
        match = match.trim();
        match = unparserUtil.nl2br(match);
        match = unparserUtil.removeHtml(match);
        match = unparserUtil.removeTrailingHtmlNewlines(match);
        match = unparserUtil.unescapeHtml(match);
        match = match.trim();
        return MessageFormat.format(REPLACEMENT, match);
    }

    @Override
    protected int getFlags() {
        return Pattern.DOTALL;
    }
}
