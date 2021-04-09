package interfax.rest.client.upload;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Sharing.
 * オプション選択肢の定義
 * 
 * @see [Uploading Documents][Create]
 */
public enum Sharing {

    ////////////////////////////////////////////////////////////////////////
    // enum(s)
    Private("private"),
    Shared("shared");

    ////////////////////////////////////////////////////////////////////////
    // field(s)
    @Getter
    private final String define;
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<Sharing> deserializer =
        (JsonDeserializer<Sharing>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            Sharing result = Sharing.valueOf(json.getAsString());
            return(result);
        };
    
    ////////////////////////////////////////////////////////////////////////
    // constructor

    private Sharing(String define) {
        this.define = define;
    }
}
