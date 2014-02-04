package at.rueckgr.chatbox.ejb;

import java.util.concurrent.Future;


public interface ChatboxWorker {
	public Future<String> run();
}
