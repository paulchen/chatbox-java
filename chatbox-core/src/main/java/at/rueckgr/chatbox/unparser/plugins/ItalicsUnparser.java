package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItalicsUnparser extends AbstractPatternUnparser {
    private static final String[][] REPLACEMENTS = {
            {"<i>", "[i]"},
            {"<\\/i>", "[/i]"}
    };

    @Override
    protected String[][] getReplacements() {
        return REPLACEMENTS;
    }
}
