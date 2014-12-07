package at.rueckgr.chatbox.wrapper.exception;

public class PostException extends ChatboxWrapperException {
    private static final long serialVersionUID = 1088040747004319116L;

    public PostException() {
    }

    public PostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostException(String message) {
        super(message);
    }

    public PostException(Throwable cause) {
        super(cause);
    }
}
