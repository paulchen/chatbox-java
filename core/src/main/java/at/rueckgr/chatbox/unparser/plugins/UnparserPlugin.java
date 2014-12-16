package at.rueckgr.chatbox.unparser.plugins;

import at.rueckgr.chatbox.Plugin;

public interface UnparserPlugin extends Plugin {
    String unparse(String input);
}
