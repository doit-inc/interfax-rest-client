package interfax.rest.client.sending;

import java.util.List;
import lombok.Getter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import interfax.rest.client.QueryStringParameter;
import javax.xml.bind.annotation.XmlElement;
/**
 * Fax Destination.
 * FAX送信リクエスト時に利用する、送付先情報表現です。
 * URLに対して、Query String として投入されます。
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/send-fax/
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/send-batch/
 */
public class FaxDestination implements QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    @XmlElement
    private final String faxNumber;
    
    @Getter
    @XmlElement
    private final String contact;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * query string の追加設定.
     * @param params 追加先
     */
    @Override
    public void addQueryString(List<NameValuePair> params) {
        params.add(new BasicNameValuePair("faxNumber", faxNumber));
        if(contact != null) {
            params.add(new BasicNameValuePair("contact", contact));
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static nitializer
    
    public FaxDestination(String faxNumber, String contact) {
        this.faxNumber = faxNumber;
        this.contact = contact;
    }
}
