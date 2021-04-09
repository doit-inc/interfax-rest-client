package interfax.rest.client.upload;

import interfax.rest.client.DateDeserializer;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
/**
 * [Uploading Documents][Get List].
 * 表題の API呼出で返される値の表現
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/get-list/
 */
public class UploadedContentStatus {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private String userId;

    @Getter @Setter
    private String fileName;

    @Getter @Setter
    private int fileSize;
    
    @Getter @Setter
    private int uploaded;

    @Getter @Setter
    private String uri;
    
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date creationTime;
    
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date lastusageTime;
    
    @Getter @Setter
    private Status status;
    
    @Getter @Setter
    private Disposition disposition;
    
    @Getter @Setter
    private Sharing sharing;
}
