package at.rueckgr.chatbox.wrapper.exception;

// TODO create subclasses for each problem that can occur in ChatboxImpl
public abstract class ChatboxWrapperException extends Exception {
    private static final long serialVersionUID = 8933248215946665278L;

    public ChatboxWrapperException() {
        super();
    }

    public ChatboxWrapperException(String message, Throwable cause,
                                   boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ChatboxWrapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatboxWrapperException(String message) {
        super(message);
    }

    public ChatboxWrapperException(Throwable cause) {
        super(cause);
    }
}
