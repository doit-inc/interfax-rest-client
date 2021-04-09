package interfax.rest.client.addressbooks;

import interfax.rest.client.QueryStringParameter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * InterFAX REST [Address Book].
 * [Addressbooks][Get Addressbooks List] で投入する Query Parameter
 */
public class GetAddressListOptions implements QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Setter @Getter
    private Integer limit;
    
    @Setter @Getter
    private String offset;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    @Override
    public void addQueryString(List<NameValuePair> params) {
        if(limit != null) {
            params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }
        if(offset != null) {
            params.add(new BasicNameValuePair("offset", offset));
        }
    }
}
