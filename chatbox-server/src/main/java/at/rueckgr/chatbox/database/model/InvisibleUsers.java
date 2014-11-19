package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * The persistent class for the invisible_users database table.
 */
@Entity
@Table(name = "invisible_users")
@NamedQuery(name = InvisibleUsers.QRY_FIND_ALL, query = "SELECT i FROM InvisibleUsers i")
@Getter
@Setter
public class InvisibleUsers implements ChatboxEntity {
    private static final long serialVersionUID = 2321180948048945868L;

    public static final String QRY_FIND_ALL = "InvisibleUser.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "timestamp")
    private Date date;

    @NotNull
    private Integer users;
}
