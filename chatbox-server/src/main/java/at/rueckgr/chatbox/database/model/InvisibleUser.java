package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


/**
 * The persistent class for the invisible_users database table.
 */
@Entity
@Table(name = "invisible_users")
@NamedQuery(name = InvisibleUser.QRY_FIND_ALL, query = "SELECT i FROM InvisibleUser i")
@Getter
@Setter
public class InvisibleUser implements ChatboxEntity {
    private static final long serialVersionUID = 2321180948048945868L;

    public static final String QRY_FIND_ALL = "InvisibleUser.findAll";

    @Id
    private Integer id;

    @NotNull
    private Timestamp timestamp;

    @NotNull
    private Integer users;
}
