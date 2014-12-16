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
        ChatboxMessage notification = getGson().decode(json);
        if (notification instanceof NewMessagesNotification) {
            NewMessagesNotification newMessagesNotification = (NewMessagesNotification) notification;

            for (MessageDTO message : newMessagesNotification.getNewMessages()) {
                System.out.println("New message received: " + message.toString());
            }

            for (MessageDTO message : newMessagesNotification.getModifiedMessages()) {
                System.out.println("Modified message received: " + message.toString());
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
