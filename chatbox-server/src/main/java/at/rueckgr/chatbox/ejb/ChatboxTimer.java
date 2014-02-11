package at.rueckgr.chatbox.ejb;


public interface ChatboxTimer {

    void startup();

    void ensureWorkerRunning();

}