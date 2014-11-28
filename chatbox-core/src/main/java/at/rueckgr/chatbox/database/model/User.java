package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the users database table.
 */
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "users_category_idx", columnList = "category")
        }
)
@NamedQuery(name = User.QRY_FIND_ALL, query = "SELECT u FROM User u")
@Getter
@Setter
public class User implements ChatboxEntity {
    private static final long serialVersionUID = 5822232484553184990L;

    public static final String QRY_FIND_ALL = "User.findAll";

    @Id
    private Integer id;

    @NotNull
    @Lob
    private String name;

    //bi-directional many-to-one association to UserCategory
    @ManyToOne
    @JoinColumn(name = "category")
    private UserCategory userCategory;
}
