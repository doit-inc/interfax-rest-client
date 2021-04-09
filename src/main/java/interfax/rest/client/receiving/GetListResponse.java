package interfax.rest.client.receiving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import interfax.rest.client.MethodResponse;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
/**
 * InterFAX REST [Receiving Faxes][Get List].
 * [Receiving Faxes][Get List] で返される情値の MethodResponse 拡張.
 */
@XmlRootElement(name="ArrayOfFax")
public class GetListResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Getter
    @XmlElement(name="Fax")
    private List<ReceivingFaxStatus> receivedFaxlist;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetListResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                // TimeZone を GMT に補正する
                gsonBuilder.registerTypeAdapter(Date.class, super.getDateDeserializer());
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<ReceivingFaxStatus>>(){}.getType();
                this.receivedFaxlist = gson.fromJson(this.getBody(), collectionType);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                GetListResponse fromXML = JAXB.unmarshal(reader, GetListResponse.class);
                this.receivedFaxlist = fromXML.getReceivedFaxlist();
            }
        }
        if(receivedFaxlist == null) {
            receivedFaxlist = new ArrayList();
        }
    }
    
    // for JAXB
    private GetListResponse() {
    }
}
