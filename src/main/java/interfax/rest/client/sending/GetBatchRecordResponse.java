package interfax.rest.client.sending;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import interfax.rest.client.MethodResponse;
import java.io.StringReader;
import java.util.Date;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import lombok.Getter;
/**
 * [Sending Batches][Get Batch Record].
 * 表題の API呼出で返される値の MethodResponse 拡張.
 */
public class GetBatchRecordResponse extends MethodResponse {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private SendBatchRecordStatus status;

    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetBatchRecordResponse(Response response) {
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
                this.status = gson.fromJson(this.getBody(), SendBatchRecordStatus.class);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                status = JAXB.unmarshal(reader, SendBatchRecordStatus.class);
            }
        }
    }
}
