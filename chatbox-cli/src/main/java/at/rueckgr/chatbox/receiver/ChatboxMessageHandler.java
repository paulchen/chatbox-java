package at.rueckgr.chatbox.receiver;

import at.rueckgr.chatbox.dto.MessageDTO;
import at.rueckgr.chatbox.dto.message.ChatboxMessage;
import at.rueckgr.chatbox.dto.message.NewMessagesNotification;
import at.rueckgr.chatbox.util.GsonProcessor;
import at.rueckgr.chatbox.util.GsonProcessorImpl;

import javax.websocket.MessageHandler.Whole;
import java.io.Serializable;

public class ChatboxMessageHandler implements Whole<String>, Serializable {
    private static final long serialVersionUID = 8302910719261842187L;

    private GsonProcessor gson;

    public void onMessage(String json) {
        ChatboxMessage messages = getGson().decode(json);
        if (messages instanceof NewMessagesNotification) {
            for (MessageDTO message : ((NewMessagesNotification) messages).getMessages()) {
                System.out.println("Message received: " + message.toString());
            }
        }
    }

    private GsonProcessor getGson() {
        if (this.gson == null) {
            this.gson = new GsonProcessorImpl();
        }

        return this.gson;
    }
}
