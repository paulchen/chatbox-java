package at.rueckgr.chatbox.unparser.plugins;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IndentUnparser extends AbstractPatternUnparser {
    private static final String[][] REPLACEMENTS = {
            {"<blockquote><div>", "[indent]"},
            {"<\\/div><\\/blockquote>", "[/indent]"}
    };

    @Override
    protected String[][] getReplacements() {
        return REPLACEMENTS;
    }
}
