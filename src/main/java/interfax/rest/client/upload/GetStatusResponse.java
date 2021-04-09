package interfax.rest.client.upload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfax.rest.client.MethodResponse;
import java.io.StringReader;
import java.util.Date;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import lombok.Getter;
/**
 * [Uploading Documents][Get Status].
 * 表題の API呼出で返される値の MethodResponse 拡張.
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/get-list/
 */
public class GetStatusResponse extends MethodResponse {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private UploadedContentStatus status;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetStatusResponse(Response response) {
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
                this.status = gson.fromJson(this.getBody(), UploadedContentStatus.class);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                status = JAXB.unmarshal(reader, UploadedContentStatus.class);
            }
        }
    }
}
