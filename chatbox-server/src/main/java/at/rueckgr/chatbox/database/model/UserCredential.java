package at.rueckgr.chatbox.database.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_credentials database table.
 * 
 */
@Entity
@Table(name="user_credentials")
@NamedQuery(name="UserCredential.findAll", query="SELECT u FROM UserCredential u")
public class UserCredential implements Serializable, ChatboxEntity {
	private static final long serialVersionUID = 1L;

	@Column(name="access_token")
	private String accessToken;

	private String cookie;

	@Id
	private Integer id;

	private String password;

	private String securitytoken;

	public UserCredential() {
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecuritytoken() {
		return this.securitytoken;
	}

	public void setSecuritytoken(String securitytoken) {
		this.securitytoken = securitytoken;
	}

}