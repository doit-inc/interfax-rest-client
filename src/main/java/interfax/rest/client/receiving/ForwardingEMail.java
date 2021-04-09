package interfax.rest.client.receiving;

import interfax.rest.client.DateDeserializer;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
/**
 * [Receiving Faxes][Get Forwarding EMails].
 * 表題の REST API 呼出によって返されるデータ構造の定義
 * 
 * @see interfax.rest.client.receiving.ReceivingFaxes.getForwardingEMails()
 */
public class ForwardingEMail {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private String emailAddress;
    
    @Getter @Setter
    private int messageStatus;
    
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date completionTime;
}
