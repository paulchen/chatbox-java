package at.rueckgr.chatbox.ejb;

import java.util.concurrent.Future;


public interface ChatboxWorker {
    void loadExistingShouts();

    Future<String> run();

    void importSmilies();
}
