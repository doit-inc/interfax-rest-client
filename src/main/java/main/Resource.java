package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.Getter;
/**
 * 外部定義リソースの取得.
 * 
 * JSON で定義されている値を deserialize して供給します。
 */
public class Resource {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private String id;
    
    @Getter
    private String password;
    
    @Getter
    private String email;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static Resource get() throws IOException {
        Resource result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try ( InputStream in = Resource.class.getResourceAsStream("resources.json")) {
            InputStreamReader reader = new InputStreamReader(in);
            result = gson.fromJson(reader, Resource.class);
        }
        return(result);
    }
}
