package at.rueckgr.chatbox.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@EqualsAndHashCode(exclude = { "rawMessage" })
@Getter
@Setter
public class MessageDTO implements ChatboxDTO, Comparable<MessageDTO> {
    private static final long serialVersionUID = -6941047808863237669L;

    private Integer primaryId;
    private Integer id;
    private Integer epoch;
    private String message;
    private transient String rawMessage; // don't send raw message to clients
    private LocalDateTime date;
    private boolean deleted;
    private UserDTO user;

    public MessageDTO() {
        super();
    }

    public MessageDTO(Integer primaryId, Integer id, Integer epoch, String rawMessage, LocalDateTime date, boolean deleted, UserDTO user) {
        this.primaryId = primaryId;
        this.id = id;
        this.epoch = epoch;
        this.rawMessage = rawMessage;
        this.date = date;
        this.deleted = deleted;
        this.user = user;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String username = this.user.getName();
        String dateString = dateTimeFormatter.format(this.date);

        return MessageFormat.format("[{0}] <{1}> {2}", dateString, username, this.message);
    }

    @Override
    public int compareTo(MessageDTO that) {
        return primaryId.compareTo(that.getPrimaryId());
    }
}
