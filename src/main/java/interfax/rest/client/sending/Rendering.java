package interfax.rest.client.sending;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Rendering Mode.
 * オプション選択肢の定義
 */
public enum Rendering {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    Standard,
    Fine,
    Grayscale,  // リファレンスに定義の記載はあるケースがある為、念の為の宣言
    Bw;         // リファレンスに定義の記載はあるケースがある為、念の為の宣言
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<Rendering> deserializer =
        (JsonDeserializer<Rendering>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            Rendering result = Rendering.valueOf(json.getAsString());
            return(result);
        };
}
