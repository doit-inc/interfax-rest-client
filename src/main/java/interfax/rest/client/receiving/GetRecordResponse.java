package interfax.rest.client.receiving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfax.rest.client.MethodResponse;
import java.io.StringReader;
import java.util.Date;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import lombok.Getter;
/**
 * InterFAX REST [Receiving Faxes][Get List].
 * [Receiving Faxes][Get List] で返される値の MethodResponse 拡張.
 */
public class GetRecordResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Getter
    private ReceivingFaxStatus receivedFaxStatus;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetRecordResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                // TimeZone を GMT に補正する
                gsonBuilder.registerTypeAdapter(Date.class, super.getDateDeserializer());
                Gson gson = gsonBuilder.create();
                this.receivedFaxStatus = gson.fromJson(this.getBody(), ReceivingFaxStatus.class);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                receivedFaxStatus = JAXB.unmarshal(reader, ReceivingFaxStatus.class);
            }
        }
    }
}
