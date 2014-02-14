package at.rueckgr.chatbox.dto.message;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ChatboxMessageAdapter implements JsonSerializer<ChatboxMessage>, JsonDeserializer<ChatboxMessage>, Serializable {
    private static final long serialVersionUID = -8037050812981608616L;

    private static Log log = LogFactory.getLog(ChatboxMessageAdapter.class);

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public JsonElement serialize(ChatboxMessage message, Type type,
                                 JsonSerializationContext context) {
        JsonObject ret = new JsonObject();

        ret.addProperty(CLASSNAME, message.getClass().getCanonicalName());
        ret.add(INSTANCE, context.serialize(message));

        return ret;
    }

    @Override
    public ChatboxMessage deserialize(JsonElement json, Type type,
                                      JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
        if (primitive == null) {
            String message = "Received JSON message without classname";
            log.error(message);
            throw new JsonParseException(message);
        }
        JsonElement instance = jsonObject.get(INSTANCE);
        if (instance == null) {
            String message = "Received JSON message without instance data";
            log.error(message);
            throw new JsonParseException(message);
        }

        try {
            return context.deserialize(instance, Class.forName(primitive.getAsString()));
        }
        catch (ClassNotFoundException e) {
            String message = "Received JSON message with invalid class: " + primitive.getAsString();

            log.error(message, e);

            throw new JsonParseException(message, e);
        }
    }

}
