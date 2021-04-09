package interfax.rest.client.sending;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Page Orientation.
 * オプション選択肢の定義
 */
public enum PageOrientation {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    Portrait,
    Landscape;
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<PageOrientation> deserializer =
        (JsonDeserializer<PageOrientation>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            PageOrientation result = PageOrientation.valueOf(json.getAsString());
            return(result);
        };
}