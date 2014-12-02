package at.rueckgr.chatbox.unparser.plugins;

import java.text.MessageFormat;

public abstract class AbstractPatternUnparser extends AbstractUnparserPlugin {
    protected abstract String[][] getReplacements();

    @Override
    public String unparse(String input) {
        log.debug(MessageFormat.format("Message before unparsing using Unparser {0}: {1}", getClass().getSimpleName(), input));

        String output = input;
        for (String[] replacement : getReplacements()) {
            output = output.replaceAll(replacement[0], replacement[1]);
        }

        log.debug(MessageFormat.format("Message after unparsing using Unparser {0}: {1}", getClass().getSimpleName(), output));

        return output;
    }
}
