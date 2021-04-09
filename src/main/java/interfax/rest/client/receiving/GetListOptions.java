package interfax.rest.client.receiving;

import interfax.rest.client.QueryStringParameter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * InterFAX REST [Receiving Faxes][Get List].
 * [Receiving Faxes][Get List] で投入する Query Parameter
 */
public class GetListOptions implements QueryStringParameter {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Setter @Getter
    private Boolean unreadOnly;
    
    @Setter @Getter
    private Integer limit;
    
    @Setter @Getter
    private Long lastId;
    
    @Setter @Getter
    private Boolean allUser;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    @Override
    public void addQueryString(List<NameValuePair> params) {
        if(unreadOnly != null) {
            params.add(new BasicNameValuePair("unreadOnly", String.valueOf(unreadOnly)));
        }
        if(limit != null) {
            params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }
        if(lastId != null) {
            params.add(new BasicNameValuePair("lastId", String.valueOf(lastId)));
        }
        if(allUser != null) {
            params.add(new BasicNameValuePair("allUser", String.valueOf(allUser)));
        }
    }
}
