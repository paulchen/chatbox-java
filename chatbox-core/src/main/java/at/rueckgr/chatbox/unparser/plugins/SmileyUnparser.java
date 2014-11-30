package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.service.ChatboxWorker;
import at.rueckgr.chatbox.service.database.SmileyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author paulchen
 */
@Unparser(order = 1)
@ApplicationScoped
@Transactional
public class SmileyUnparser extends AbstractUnparserPlugin {
    private static final String SMILEY_PATTERN = "<img src=\"[^\"]*/([^\"/]+)\" border=\"0\" alt=\"[^\"]*\" title=\"[^\"]*\" class=\"inlineimg\" />";

    private @Inject Log log;
    private @Inject ChatboxWorker chatboxWorker;
    private @Inject SmileyService smileyService;

    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing smileys: {0}", input));

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

        String output = stringBuffer.toString();
        log.debug(MessageFormat.format("Message after unparsing smileys: {0}", output));

        return output;
    }

    private String findSmiley(String filename) {
        return findSmiley(filename, true);
    }

    private String findSmiley(String filename, boolean recursive) {
        Smiley smiley = smileyService.findByFilename(filename);
        if(smiley != null) {
            if (!StringUtils.isBlank(smiley.getCode())) {
                return smiley.getCode();
            }
        }
        else {
            log.debug("Smiley not found in database");
        }

        // TODO BUG: first smiley is not properly replaced if it has just been imported from the database
        if(!recursive) {
            return "";
        }

        chatboxWorker.importSmilies();

        return findSmiley(filename, false);
    }
}
