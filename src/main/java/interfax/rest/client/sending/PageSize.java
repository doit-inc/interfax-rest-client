package interfax.rest.client.sending;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Page Size.
 * オプション選択肢の定義
 */
public enum PageSize {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    A4,
    Letter,
    Legal,
    B4;
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<PageSize> deserializer =
        (JsonDeserializer<PageSize>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            PageSize result = PageSize.valueOf(json.getAsString());
            return(result);
        };
}
