package at.rueckgr.chatbox.wrapper;

import at.rueckgr.chatbox.dto.MessageDTO;

import java.util.List;

public interface Chatbox {

    void login() throws ChatboxWrapperException;

    List<MessageDTO> fetchCurrent() throws ChatboxWrapperException;

    List<MessageDTO> fetchArchive(int page) throws ChatboxWrapperException;

    boolean post(String message) throws Exception;

    void setSession(ChatboxSession chatboxSession);

}