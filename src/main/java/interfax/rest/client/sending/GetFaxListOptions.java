package interfax.rest.client.sending;

import interfax.rest.client.QueryStringParameter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * InterFAX REST [Sending Faxes][Get Fax List].
 * [Sending Faxes][Get Fax List] で投入する Query Parameter
 */
public class GetFaxListOptions implements QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Setter @Getter
    private Integer limit;
    
    @Setter @Getter
    private String lastId;
    
    @Setter @Getter
    private SortOrder sortOrder;
    
    @Setter @Getter
    private String userId;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    @Override
    public void addQueryString(List<NameValuePair> params) {
        if(limit != null) {
            params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }
        if(lastId != null) {
            params.add(new BasicNameValuePair("lastId", lastId));
        }
        if(sortOrder != null) {
            params.add(new BasicNameValuePair("sortOrder", sortOrder.name()));
        }
        if(userId != null) {
            params.add(new BasicNameValuePair("userId", userId));
        }
    }
}
