package at.rueckgr.chatbox.wrapper.exception;

import java.text.MessageFormat;

public class WrongMessageCountException extends ChatboxWrapperException {
    private static final long serialVersionUID = -2144288426892954804L;

    private final int expected;
    private final int actual;
    private final boolean archive;
    private final String url;

    public WrongMessageCountException(int expected, int actual, boolean archive, String url) {
        this.expected = expected;
        this.actual = actual;
        this.archive = archive;
        this.url = url;
    }

    public int getActual() {
        return actual;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isArchive() {
        return archive;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return MessageFormat.format("Wrong number of messages fetched from chatbox (expected {0}, actual {1})", expected, actual);
    }
}
