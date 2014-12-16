package at.rueckgr.chatbox.wrapper.exception;

public class PollingException extends ChatboxWrapperException {
    private static final long serialVersionUID = 1088040747004319116L;

    public PollingException() {
    }

    public PollingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PollingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollingException(String message) {
        super(message);
    }

    public PollingException(Throwable cause) {
        super(cause);
    }
}
