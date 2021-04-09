package interfax.rest.client.sending;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import interfax.rest.client.MethodResponse;
import java.io.PrintStream;
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
 * [Sending Faxes][GetFaxList].
 * 表題の API呼出で返される値の MethodResponse 拡張.
 */
@XmlRootElement(name="ArrayOfFaxSummary")
public class GetFaxListResponse  extends MethodResponse {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    @XmlElement(name="FaxSummary")
    private List<SendFaxStatus> statusList;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer

    public GetFaxListResponse(Response response, PrintStream ps) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, super.getDateDeserializer());
                gsonBuilder.registerTypeAdapter(PageSize.class, PageSize.getDeserializer());
                gsonBuilder.registerTypeAdapter(PageOrientation.class, PageOrientation.getDeserializer());
                gsonBuilder.registerTypeAdapter(Resolution.class, Resolution.getDeserializer());
                gsonBuilder.registerTypeAdapter(Rendering.class, Rendering.getDeserializer());
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<SendFaxStatus>>(){}.getType();
                this.statusList = gson.fromJson(this.getBody(ps), collectionType);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                GetFaxListResponse fromXML = JAXB.unmarshal(reader, GetFaxListResponse.class);
                this.statusList = fromXML.getStatusList();
            }
        }
        if(statusList == null) {
            statusList = new ArrayList();
        }
    }
    
    // for JAXB
    private GetFaxListResponse() {
    }
}
