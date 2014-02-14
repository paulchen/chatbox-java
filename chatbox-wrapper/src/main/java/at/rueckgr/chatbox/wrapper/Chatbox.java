package at.rueckgr.chatbox.wrapper;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.io.Serializable;
import java.util.List;

public interface Chatbox extends Serializable {

    void login() throws ChatboxWrapperException;

    List<MessageDTO> fetchCurrent() throws ChatboxWrapperException;

    List<MessageDTO> fetchArchive(int page) throws ChatboxWrapperException;

    boolean post(String message) throws Exception;

    void setSession(ChatboxSession chatboxSession);

}