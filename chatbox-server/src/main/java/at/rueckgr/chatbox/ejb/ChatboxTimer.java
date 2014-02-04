package at.rueckgr.chatbox.ejb;


public interface ChatboxTimer {

	public abstract void startup();

	public abstract void ensureWorkerRunning();

}