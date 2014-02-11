package at.rueckgr.chatbox.ejb;

import java.util.concurrent.Future;


public interface ChatboxWorker {
    Future<String> run();
}
