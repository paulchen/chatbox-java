package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;

@Unparser
@ApplicationScoped
public class UnderlineUnparser extends AbstractPatternUnparser {
    private static final String[][] REPLACEMENTS = {
            {"<u>", "[u]"},
            {"<\\/u>", "[/u]"}
    };

    @Override
    protected String[][] getReplacements() {
        return REPLACEMENTS;
    }
}
