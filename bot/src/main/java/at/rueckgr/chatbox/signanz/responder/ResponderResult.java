package at.rueckgr.chatbox.signanz.responder;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponderResult {
    private final String message;
    private final List<String> messagesToPost;

    public ResponderResult(String message, List<String> messagesToPost) {
        this.message = message;
        this.messagesToPost = messagesToPost;
    }
}
