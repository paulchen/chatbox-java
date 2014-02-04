package at.rueckgr.chatbox.dto;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageDTO implements Serializable {
	private static final long serialVersionUID = -6941047808863237669L;
	
	private final int id;
	private final int epoch;
	private final String message;
	private final Date date;
	private final boolean deleted;
	private final UserDTO user;
	
	public MessageDTO(int id, int epoch, String message, Date date,
			boolean deleted, UserDTO user) {
		super();
		this.id = id;
		this.epoch = epoch;
		this.message = message;
		this.date = date;
		this.deleted = deleted;
		this.user = user;
	}

	public int getId() {
		return id;
	}
	
	public int getEpoch() {
		return epoch;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Date getDate() {
		return date;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public UserDTO getUser() {
		return user;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		return String.format("[%s] <%s> %s", dateFormat.format(this.date), this.user.getName(), this.message);
	}
}
