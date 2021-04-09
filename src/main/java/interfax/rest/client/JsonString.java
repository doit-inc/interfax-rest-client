package interfax.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
/**
 * JSON 操作ユーティリティ.
 */
public class JsonString {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    /**
     * JSON 文字列の整形.
     * 直列化している JSON 文字列を、視認し易いように整形します。
     * 
     * @param buffer 整形後文字列の投入先
     * @param json 整形 JSON 文字列
     */
    public static void format(StringBuilder buffer, String json) {
        //
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        //
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        buffer.append(gson.toJson(element));
    }
}
