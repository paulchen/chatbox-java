package at.rueckgr.chatbox.wrapper.exception;

public class OtherProblemException extends ChatboxWrapperException {
    private static final long serialVersionUID = -4458475712898142476L;

    public OtherProblemException() {
    }

    public OtherProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OtherProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtherProblemException(String message) {
        super(message);
    }

    public OtherProblemException(Throwable cause) {
        super(cause);
    }
}
