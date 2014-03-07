package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author paulchen
 */
@Unparser
@ApplicationScoped
public class SmileyUnparser extends AbstractUnparserPlugin implements Serializable {
    private static final long serialVersionUID = -1805868547159120543L;

    private static final String SMILEY_PATTERN = "<img src=\"[^\"]*/([^\"/]+)\" border=\"0\" alt=\"[^\"]*\" title=\"[^\"]*\" class=\"inlineimg\" />";

    @Inject
    private EntityManager entityManager;

    @Override
    public String unparse(String input) {
        System.out.println(input);

        // taken from: http://www.javamex.com/tutorials/regular_expressions/search_replace_loop.shtml
        Pattern pattern = Pattern.compile(SMILEY_PATTERN);
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer(input.length());

        while(matcher.find()) {
            String filename = matcher.group(1);
            String smileyCode = findSmiley(filename);
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(smileyCode));
        }
        matcher.appendTail(stringBuffer);

        System.out.println(stringBuffer);

        return stringBuffer.toString();
    }

    private String findSmiley(String filename) {
        return "XXX";
    }
}
