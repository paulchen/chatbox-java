package at.rueckgr.chatbox.unparser.plugins;

import org.apache.commons.logging.Log;

import javax.inject.Inject;
import java.text.MessageFormat;

public abstract class AbstractPatternUnparser extends AbstractUnparserPlugin {
    protected abstract String[][] getReplacements();

    private @Inject Log log;

    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing italics: {0}", input));

        String output = input;
        for (String[] replacement : getReplacements()) {
            output = output.replaceAll(replacement[0], replacement[1]);
        }

        log.debug(MessageFormat.format("Message after unparsing italics: {0}", output));

        return output;
    }
}
