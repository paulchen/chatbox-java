package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the user_credentials database table.
 */
@Entity
@Table(name = "user_credentials")
@NamedQuery(name = UserCredential.QRY_FIND_ALL, query = "SELECT u FROM UserCredential u")
@Getter
@Setter
public class UserCredential implements ChatboxEntity {
    private static final long serialVersionUID = -4006467371621683138L;

    public static final String QRY_FIND_ALL = "UserCredential.findAll";

    @Column(name = "access_token")
    @Lob
    private String accessToken;

    @Lob
    private String cookie;

    @Id
    @OneToOne
    @JoinColumn(name = "id")
    private User id;

    @Lob
    private String password;

    @Lob
    private String securitytoken;
}
