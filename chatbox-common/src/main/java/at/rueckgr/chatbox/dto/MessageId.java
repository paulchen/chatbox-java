package at.rueckgr.chatbox.dto;

import java.io.Serializable;

/**
 * @author paulchen
 */
// TODO make this class immutable?
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageId messageId = (MessageId) o;

        if (epoch != messageId.epoch) {
            return false;
        }
        if (id != messageId.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + epoch;
        return result;
    }

    @Override
    public int compareTo(MessageId that) {
        // TODO that may be null
        if(this.epoch != that.epoch) {
            return Integer.compare(this.epoch, that.epoch);
        }
        return Integer.compare(this.id, that.id);
    }
}
