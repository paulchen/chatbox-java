package at.rueckgr.chatbox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@EqualsAndHashCode(exclude = { "rawMessage" })
@Getter
@Setter
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

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String username = this.user.getName();
        String dateString = dateFormat.format(this.date);

        return MessageFormat.format("[{0}] <{1}> {2}", dateString, username, this.message);
    }

    @Override
    public int compareTo(MessageDTO that) {
        return messageId.compareTo(that.getMessageId());
    }

    public boolean equalsRaw(MessageDTO that) {
        if(!this.equals(that)) {
            return false;
        }
        if (this == that) {
            return true;
        }

        if (rawMessage != null ? !rawMessage.equals(that.rawMessage) : that.rawMessage != null) {
            return false;
        }

        return true;
    }
}
