package at.rueckgr.chatbox.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OnlineUsersInfo implements Serializable {
    private static final long serialVersionUID = 7168615023759235488L;

    private List<UserDTO> onlineUsers;
    private int invisibleUsers;

    public OnlineUsersInfo(List<UserDTO> onlineUsers, int invisibleUsers) {
        this.onlineUsers = onlineUsers;
        this.invisibleUsers = invisibleUsers;
    }
}
