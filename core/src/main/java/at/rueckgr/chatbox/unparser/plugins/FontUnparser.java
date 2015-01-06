package at.rueckgr.chatbox.unparser.plugins;

import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import java.text.MessageFormat;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class FontUnparser extends AbstractUnparserPlugin {
    @Getter
    private enum FontPattern {
        COLOR("<font color=\"?([^\"]+)\"?>", "color", "[color={0}]", "[/color]"),
        SIZE("<font size=\"?([^\"]+)\"?>", "size", "[size={0}]", "[/size]"),
        FACE("<font face=\"?([^\"]+)\"?>", "face", "[font={0}]", "[/font]");

        private String pattern;
        private String discriminator;
        private String startTag;
        private String endTag;

        private FontPattern(String pattern, String discriminator, String startTag, String endTag) {
            this.pattern = pattern;
            this.discriminator = discriminator;
            this.startTag = startTag;
            this.endTag = endTag;
        }
    }

    private static final String END_TAG_PATTERN = "</font>";

    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing using Unparser {0}: {1}", getClass().getSimpleName(), input));

        StringBuilder superPatternBuilder = new StringBuilder("(");
        for (FontPattern fontPattern : FontPattern.values()) {
            superPatternBuilder.append(fontPattern.getPattern());
            superPatternBuilder.append("|");
        }
        superPatternBuilder.append(END_TAG_PATTERN).append(")");
        String superPattern = superPatternBuilder.toString();

        Stack<FontPattern> tags = new Stack<FontPattern>();

        // taken from: http://www.javamex.com/tutorials/regular_expressions/search_replace_loop.shtml
        Pattern pattern = Pattern.compile(superPattern);
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer(input.length());

        while(matcher.find()) {
            String match = matcher.group(0);
            FontPattern tag = null;
            for (FontPattern fontPattern : FontPattern.values()) {
                if(match.contains(fontPattern.getDiscriminator())) {
                    tag = fontPattern;
                    break;
                }
            }
            if(tag == null) {
                if(!match.matches(END_TAG_PATTERN)) {
                    throw new RuntimeException();
                }
                tag = tags.pop();
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(tag.getEndTag()));
            }
            else {
                tags.push(tag);
                matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(MessageFormat.format(tag.getStartTag(), findLastNonEmptyGroup(matcher))));
            }
        }
        matcher.appendTail(stringBuffer);

        String output = stringBuffer.toString();

        log.debug(MessageFormat.format("Message after unparsing using Unparser {0}: {1}", getClass().getSimpleName(), output));

        return output;
    }

    private String findLastNonEmptyGroup(Matcher matcher) {
        String ret = null;
        for(int i = 1; i <= matcher.groupCount(); i++) {
            String group = matcher.group(i);
            if(group != null) {
                ret = group;
            }
        }

        if(ret == null) {
            throw new RuntimeException();
        }
        return ret;
    }
}
