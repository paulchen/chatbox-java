package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.database.model.Smiley;
import at.rueckgr.chatbox.ejb.ChatboxWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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
    private Log log;

    @Inject
    private EntityManager em;

    @Inject
    private ChatboxWorker chatboxWorker;

    @Override
    public String unparse(String input) {
        log.debug(String.format("Message before unparsing smileys: %s", input));

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

        log.debug(String.format("Message after unparsing smileys: %s", input));

        return stringBuffer.toString();
    }

    private String findSmiley(String filename) {
        return findSmiley(filename, true);
    }

    private String findSmiley(String filename, boolean recursive) {
        TypedQuery<Smiley> query = em.createNamedQuery(Smiley.FIND_BY_FILENAME, Smiley.class);
        query.setParameter("filename", filename);

        try {
            Smiley smiley = query.getSingleResult();
            if(!StringUtils.isBlank(smiley.getCode())) {
                return smiley.getCode();
            }
        }
        catch (NoResultException e) {
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
