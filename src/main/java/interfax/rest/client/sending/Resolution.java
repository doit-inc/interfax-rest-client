package interfax.rest.client.sending;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Resolution.
 * オプション選択肢の定義
 */
public enum Resolution {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    Standard,
    Fine;
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<Resolution> deserializer =
        (JsonDeserializer<Resolution>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            Resolution result = Resolution.valueOf(json.getAsString());
            return(result);
        };
}
