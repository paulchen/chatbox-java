package at.rueckgr.chatbox.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageDTO implements Serializable, ChatboxDTO, Comparable<MessageDTO> {
    private static final long serialVersionUID = -6941047808863237669L;

    private MessageId messageId;
    private String message;
    private transient String rawMessage; // don't send raw message to clients
    private Date date;
    private boolean deleted;
    private UserDTO user;

    public MessageDTO() {
        super();
    }

    public MessageDTO(MessageId messageId, String rawMessage, Date date, boolean deleted, UserDTO user) {
        this.messageId = messageId;
        this.rawMessage = rawMessage;
        this.date = date;
        this.deleted = deleted;
        this.user = user;
    }

    public MessageId getMessageId() {
        return messageId;
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

    public void setMessageId(MessageId messageId) {
        this.messageId = messageId;
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

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return String.format("[%s] <%s> %s", dateFormat.format(this.date), this.user.getName(), this.message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO that = (MessageDTO) o;

        if (deleted != that.deleted) {
            return false;
        }
        if (date != null ? !date.equals(that.date) : that.date != null) {
            return false;
        }
        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }
        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(MessageDTO that) {
        return messageId.compareTo(that.getMessageId());
    }
}
