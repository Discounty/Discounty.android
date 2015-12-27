package discounty.com.deserializers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * This class is created for deserialization of nested
 * JSON data into separate java models.
 */
public class DiscountyDeserializer<T> implements JsonDeserializer<T> {

    private final Class nestedClazz;

    private final Object nestedDeserializer;

    public DiscountyDeserializer(Class nestedClazz, Object nestedDeserializer) {
        this.nestedClazz = nestedClazz;

        this.nestedDeserializer = nestedDeserializer;
    }

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        // Get the 'discount_cards' element from the aprsed json
        JsonElement discountCards = je.getAsJsonObject().get("discount_cards");

        // Deserialize it
        GsonBuilder builder = new GsonBuilder();

        if (nestedClazz != null && nestedDeserializer != null) {
            builder.registerTypeAdapter(nestedClazz, nestedDeserializer);
        }

        return builder.create().fromJson(discountCards, type);
    }
}
