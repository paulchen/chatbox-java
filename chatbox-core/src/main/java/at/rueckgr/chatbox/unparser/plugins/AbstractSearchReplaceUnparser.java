package at.rueckgr.chatbox.unparser.plugins;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSearchReplaceUnparser extends AbstractUnparserPlugin implements UnparserPlugin {
    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing using Unparser {0}: {1}", getClass().getSimpleName(), input));

        // taken from: http://www.javamex.com/tutorials/regular_expressions/search_replace_loop.shtml
        Pattern pattern = Pattern.compile(getPattern(), getFlags());
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer(input.length());

        while(matcher.find()) {
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(getReplacement(matcher)));
        }
        matcher.appendTail(stringBuffer);

        String output = stringBuffer.toString();

        log.debug(MessageFormat.format("Message after unparsing using Unparser {0}: {1}", getClass().getSimpleName(), output));

        return output;
    }

    protected abstract String getPattern();

    protected abstract String getReplacement(Matcher matcher);

    protected int getFlags() {
        return 0;
    }
}
