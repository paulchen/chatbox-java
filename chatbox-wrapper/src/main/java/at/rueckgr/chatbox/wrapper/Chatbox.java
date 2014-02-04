package at.rueckgr.chatbox.wrapper;

import java.util.List;

import at.rueckgr.chatbox.dto.MessageDTO;

public interface Chatbox {

	public void login() throws ChatboxWrapperException;

	public List<MessageDTO> fetchCurrent() throws ChatboxWrapperException;

	public List<MessageDTO> fetchArchive(int page) throws ChatboxWrapperException;

	public boolean post(String message) throws Exception;

	public void setSession(ChatboxSession chatboxSession);

}