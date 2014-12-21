package at.rueckgr.chatbox.wrapper;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.OnlineUsersInfo;
import at.rueckgr.chatbox.dto.SmileyDTO;
import at.rueckgr.chatbox.wrapper.exception.ChatboxWrapperException;

import java.io.Serializable;
import java.util.List;

public interface Chatbox extends Serializable {

    void login() throws ChatboxWrapperException;

    List<MessageDTO> fetchCurrent() throws ChatboxWrapperException;

    List<MessageDTO> fetchArchive(int page) throws ChatboxWrapperException;

    void post(String message) throws Exception;

    void setSession(ChatboxSession chatboxSession);

    boolean hasSession();

    List<SmileyDTO> fetchSmilies() throws ChatboxWrapperException;

    OnlineUsersInfo fetchOnlineUsers() throws ChatboxWrapperException;
}
