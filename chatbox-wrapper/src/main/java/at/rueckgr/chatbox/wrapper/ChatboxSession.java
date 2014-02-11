package at.rueckgr.chatbox.wrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.Serializable;


public class ChatboxSession implements Serializable {
    private static final long serialVersionUID = -3801647039685048664L;

    private Log log = LogFactory.getLog(this.getClass());

    private final String username;
    private final String password;
    private String securityToken;
    private final BasicCookieStore cookieStore;

    public ChatboxSession(String username, String password) {
        this.username = username;
        this.password = password;

        // TODO get rid of implementation-specific class?
        this.cookieStore = new BasicCookieStore();
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected String getSecurityToken() {
        return securityToken;
    }

    protected void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void logUserdata() {
        log.debug("Current username: " + this.username);
        log.trace("Current password: " + this.password);
        log.trace("Current security token: " + this.securityToken);
    }
}
