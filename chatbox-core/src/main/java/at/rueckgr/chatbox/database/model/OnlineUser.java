package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * The persistent class for the online_users database table.
 */
@Entity
@Table(name = "online_users",
        indexes = {
                @Index(name = "online_users_user_id_idx", columnList = "user")
        }
)
@NamedQuery(name = OnlineUser.QRY_FIND_ALL, query = "SELECT o FROM OnlineUser o")
@Getter
@Setter
public class OnlineUser implements ChatboxEntity {
    private static final long serialVersionUID = -2626110206339349530L;

    public static final String QRY_FIND_ALL = "OnlineUser.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "online_users_id_seq")
    @SequenceGenerator(name = "online_users_id_seq", sequenceName = "online_users_id_seq")
    private Integer id;

    @NotNull
    @Column(name = "timestamp")
    private Date date;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
