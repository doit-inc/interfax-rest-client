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
 * InterFAX REST [Receiving Faxes][Get Forwarding EMails].
 * [Receiving Faxes][Get Forwarding EMails] で返される値の MethodResponse 拡張.
 */
@XmlRootElement(name="ArrayOfEmail")
public class GetForwardingEMailsResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Getter
    @XmlElement(name="Email")
    private List<ForwardingEMail> forwardingEMailList;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetForwardingEMailsResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                // TimeZone を GMT に補正する
                gsonBuilder.registerTypeAdapter(Date.class, super.getDateDeserializer());
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<ForwardingEMail>>(){}.getType();
                this.forwardingEMailList = gson.fromJson(this.getBody(), collectionType);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                GetForwardingEMailsResponse fromXML = JAXB.unmarshal(reader, GetForwardingEMailsResponse.class);
                this.forwardingEMailList = fromXML.getForwardingEMailList();
            }
        }
        if(forwardingEMailList == null) {
            forwardingEMailList = new ArrayList();
        }
    }
    
    // for JAXB
    private GetForwardingEMailsResponse() {
    }
}
