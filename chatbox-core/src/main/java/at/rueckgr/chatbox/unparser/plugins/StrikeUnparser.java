package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;

@Unparser
@ApplicationScoped
public class StrikeUnparser extends AbstractPatternUnparser {
    private static final String[][] REPLACEMENTS = {
            {"<strike>", "[strike]"},
            {"<\\/strike>", "[/strike]"}
    };

    @Override
    protected String[][] getReplacements() {
        return REPLACEMENTS;
    }
}
