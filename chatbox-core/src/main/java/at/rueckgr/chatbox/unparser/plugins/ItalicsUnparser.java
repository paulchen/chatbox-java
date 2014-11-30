package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.MessageFormat;

@Unparser(order = 2)
@ApplicationScoped
public class ItalicsUnparser extends AbstractUnparserPlugin {
    private static final String[][] REPLACEMENTS = {
            {"<i>", "[i]"},
            {"<\\/i>", "[/i]"}
    };
    private @Inject Log log;

    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing italics: {0}", input));

        String output = input;
        for (String[] replacement : REPLACEMENTS) {
            output = output.replaceAll(replacement[0], replacement[1]);
        }

        log.debug(MessageFormat.format("Message after unparsing italics: {0}", output));

        return output;
    }
}
