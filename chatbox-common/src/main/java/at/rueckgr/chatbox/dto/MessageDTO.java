package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageDTO implements Serializable, ChatboxDTO {
    private static final long serialVersionUID = -6941047808863237669L;

    private int id;
    private int epoch;
    private String message;
    private Date date;
    private boolean deleted;
    private UserDTO user;

    public MessageDTO() {
        super();
    }

    public MessageDTO(int id, int epoch, String message, Date date, boolean deleted, UserDTO user) {
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
        return new Date(date.getTime());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return String.format("[%s] <%s> %s", dateFormat.format(this.date), this.user.getName(), this.message);
    }
}
