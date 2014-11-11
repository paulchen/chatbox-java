package at.rueckgr.chatbox.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author paulchen
 */
// TODO make this class immutable?
@Data
public class MessageId implements DTOThing, Serializable, Comparable<MessageId> {
    private static final long serialVersionUID = 1139675659860664788L;

    private int id;
    private int epoch;

    public MessageId(int id, int epoch) {
        this.id = id;
        this.epoch = epoch;
    }

    public MessageId() {
    }

    // TODO duplicates functionality in MessageIdSorter
    @Override
    public int compareTo(MessageId that) {
        // TODO that may be null
        if(this.epoch != that.epoch) {
            return Integer.compare(this.epoch, that.epoch);
        }
        return Integer.compare(this.id, that.id);
    }
}
