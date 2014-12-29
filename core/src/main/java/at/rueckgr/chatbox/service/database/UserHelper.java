package at.rueckgr.chatbox.service.database;

import at.rueckgr.chatbox.dto.UserDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * Methods which must not be run within a transaction
 */
@ApplicationScoped
public class UserHelper {
    private @Inject UserService userService;

    /**
     * Inserts and/or updates all users from the specified stream in the database.
     *
     * This method is required as there are two concurrent threads which may need to add new users to the database
     * ({@link at.rueckgr.chatbox.service.ChatboxWorker} and {@link at.rueckgr.chatbox.service.UserWorker}).
     * Without this method, each thread starts its own transaction and adds its users within that transaction. If
     * both threads try to add the same user at the same time, one of the transaction fails.
     *
     * This method must not be called within a transaction. If it is used within a transaction, this transaction may
     * fail because the users added here may be added elsewhere concurrently.
     *
     * @param usersStream stream containing all the users to be added/updated
     */
    public void checkUsers(Stream<UserDTO> usersStream) {
        synchronized (this) {
            usersStream.forEach(userService::findUser);
        }
    }
}
