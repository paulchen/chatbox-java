package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoldUnparser extends AbstractPatternUnparser {
    private static final String[][] REPLACEMENTS = {
            {"<b>", "[b]"},
            {"<\\/b>", "[/b]"}
    };

    @Override
    protected String[][] getReplacements() {
        return REPLACEMENTS;
    }
}
