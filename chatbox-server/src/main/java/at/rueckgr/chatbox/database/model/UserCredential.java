package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the user_credentials database table.
 */
@Entity
@Table(name = "user_credentials")
@NamedQuery(name = "UserCredential.findAll", query = "SELECT u FROM UserCredential u")
@Getter
@Setter
public class UserCredential implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "access_token")
    private String accessToken;

    private String cookie;

    @Id
    private Integer id;

    private String password;

    private String securitytoken;
}
