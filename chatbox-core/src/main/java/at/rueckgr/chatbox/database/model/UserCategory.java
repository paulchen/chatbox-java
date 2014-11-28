package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the user_categories database table.
 */
@Entity
@Table(name = "user_categories")
@NamedQueries({
        @NamedQuery(name = UserCategory.QRY_FIND_ALL, query = "SELECT u FROM UserCategory u"),
        @NamedQuery(name = UserCategory.QRY_FIND_BY_NAME, query = "SELECT u FROM UserCategory u WHERE u.name = :name"),
})
@Getter
@Setter
public class UserCategory implements ChatboxEntity {
    private static final long serialVersionUID = -9187365628927287424L;

    public static final String QRY_FIND_ALL = "UserCategory.findAll";
    public static final String QRY_FIND_BY_NAME = "UserCategory.findByName";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_categories_id_seq1")
    @SequenceGenerator(name = "user_categories_id_seq1", sequenceName = "user_categories_id_seq1")
    private Integer id;

    @NotNull
    @Lob
    private String color;

    @NotNull
    @Lob
    private String name;
}
