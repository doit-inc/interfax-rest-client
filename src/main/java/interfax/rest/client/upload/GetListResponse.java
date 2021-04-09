package interfax.rest.client.upload;

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
 * [Uploading Documents][Get List].
 * 表題の API呼出で返される値の MethodResponse 拡張.
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/get-list/
 */
@XmlRootElement(name="ArrayOfUploadedDocument")
public class GetListResponse extends MethodResponse {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    @XmlElement(name="UploadedDocument")
    private List<UploadedContentStatus> statusList;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetListResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                // TimeZone を GMT に補正する
                gsonBuilder.registerTypeAdapter(Date.class, super.getDateDeserializer());
                gsonBuilder.registerTypeAdapter(Status.class, Status.getDeserializer());
                gsonBuilder.registerTypeAdapter(Sharing.class, Sharing.getDeserializer());
                gsonBuilder.registerTypeAdapter(Disposition.class, Disposition.getDeserializer());
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<UploadedContentStatus>>(){}.getType();
                this.statusList = gson.fromJson(this.getBody(), collectionType);
            } else {
                StringReader reader = new StringReader(this.getBody());
                GetListResponse fromXML = JAXB.unmarshal(reader, GetListResponse.class);
                this.statusList = fromXML.getStatusList();
            }
        }
        if(statusList == null) {
            statusList = new ArrayList();
        }
    }

    // for JAXB
    private GetListResponse() {
    }
}
