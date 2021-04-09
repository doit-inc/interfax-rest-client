package interfax.rest.client.upload;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Disposition.
 * オプション選択肢の定義
 * 
 * @see [Uploading Documents][Create].
 */
public enum Disposition {

    ////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    SingleUse,  // can be used once,
    MultiUse,   // deleted 60 minutes after the last usage
    Permanent;  // remains available until removed.
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<Disposition> deserializer =
        (JsonDeserializer<Disposition>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            Disposition result = Disposition.valueOf(json.getAsString());
            return(result);
        };
}
