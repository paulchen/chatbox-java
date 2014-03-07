package at.rueckgr.chatbox.util;

import at.rueckgr.chatbox.dto.message.ChatboxMessage;
import at.rueckgr.chatbox.dto.message.ChatboxMessageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class GsonProcessorImpl implements GsonProcessor, Serializable {
    private static final long serialVersionUID = 8995984935640412730L;

    private transient Gson gson;

    @Override
    public String encode(ChatboxMessage message) {
        return this.getGson().toJson(message, ChatboxMessage.class);
    }

    @Override
    public ChatboxMessage decode(String message) {
        return this.getGson().fromJson(message, ChatboxMessage.class);
    }

    private Gson getGson() {
        if (this.gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChatboxMessage.class, new ChatboxMessageAdapter());
            this.gson = builder.create();
        }

        return this.gson;
    }
}
