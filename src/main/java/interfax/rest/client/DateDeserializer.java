package interfax.rest.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.XmlAdapter;
/**
 * Date Deserializer.
 * JSON/XML(JAXB) 記載の Time Zone 指定の無い時刻情報解析時、GMT を付与します。
 * JSON の場合、GsonBuilder.registerTypeAdapter() にて事前登録を行います。
 * JAXB.unmarshal の場合、@XmlJavaTypeAdapter(DateDeserializer.class) を該当フィールドに記載します。
 */
public class DateDeserializer  extends XmlAdapter<String, Date> implements JsonDeserializer<Date> {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    private final SimpleDateFormat m_fomater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"){
        {setTimeZone(TimeZone.getTimeZone("GMT"));}
   };
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    // for JSON Deserialize Adapter
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Date result = null;
        String date = json.getAsString();
        try {
            result = m_fomater.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(MethodResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(result);
    }

    // for JAXB Adapter(s)
    @Override
    public Date unmarshal(String date) throws Exception {
        return(m_fomater.parse(date));
    }

    @Override
    public String marshal(Date date) throws Exception {
        return(m_fomater.format(date));
    }
}
