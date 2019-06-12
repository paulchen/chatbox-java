package at.rueckgr.chatbox.util;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UnparserUtil {
    public String removeHtml(String message) {
        return message.replaceAll("<[^>]+>", "");
    }

    public String unescapeHtml(String input) {
        String output = input.replaceAll("&nbsp;", " ");
        return StringEscapeUtils.unescapeHtml4(output);
    }

    public String nl2br(String match) {
        return match.replaceAll("<br />", "\n");
    }

    public String removeTrailingHtmlNewlines(String match) {
        final String pattern = "<br />";

        match = match.trim();
        while (match.endsWith(pattern)) {
            match = match.substring(0, match.length()-pattern.length());
            match = match.trim();
        }

        return match;
    }
}
