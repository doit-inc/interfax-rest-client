package interfax.rest.client.upload;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import lombok.Getter;
/**
 * Status.
 * オプション選択肢の定義
 * 
 * @see [Uploading Documents][Get List]
 */
public enum Status {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    Created,
    PartiallyUploaded,
    Uploading,
    Ready,
    Deleting;
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private static final JsonDeserializer<Status> deserializer =
        (JsonDeserializer<Status>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            Status result = Status.valueOf(json.getAsString());
            return(result);
        };
}
