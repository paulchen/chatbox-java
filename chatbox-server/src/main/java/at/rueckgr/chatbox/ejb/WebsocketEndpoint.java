package at.rueckgr.chatbox.ejb;


public interface WebsocketEndpoint {

    void notify(String text);

}
