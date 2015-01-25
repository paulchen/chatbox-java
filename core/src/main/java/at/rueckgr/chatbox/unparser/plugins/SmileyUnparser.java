package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.service.ChatboxWorker;
import at.rueckgr.chatbox.service.database.SmileyService;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;

@ApplicationScoped
@Transactional
public class SmileyUnparser extends AbstractSearchReplaceUnparser {
    private static final String SMILEY_PATTERN = "<img src=\"[^\"]*/([^\"/]+)\" border=\"0\" alt=\"[^\"]*\" title=\"[^\"]*\" class=\"inlineimg\" />";

    private @Inject ChatboxWorker chatboxWorker;
    private @Inject SmileyService smileyService;

    @Override
    protected String getPattern() {
        return SMILEY_PATTERN;
    }

    @Override
    protected String getReplacement(Matcher matcher) {
        String filename = matcher.group(1);
        SortedSet<String> smileyCodes = findSmiley(filename);
        if(smileyCodes.isEmpty()) {
            return "";
        }
        return smileyCodes.first();
    }

    private SortedSet<String> findSmiley(String filename) {
        return findSmiley(filename, true);
    }

    private SortedSet<String> findSmiley(String filename, boolean recursive) {
        Smiley smiley = smileyService.findByFilename(filename);
        if(smiley != null) {
            SortedSet<String> smileyCodes = smileyService.findSmileyCodes(smiley);
            if(!smileyCodes.isEmpty()) {
                return smileyCodes;
            }
        }
        else {
            log.debug(MessageFormat.format("Smiley '{0}' not found in database", filename));
        }

        // TODO BUG: first smiley is not properly replaced if it has just been imported from the database
        if(!recursive) {
            return new TreeSet<String>();
        }

        chatboxWorker.importSmilies();

        return findSmiley(filename, false);
    }
}
