package interfax.rest.client.sending;

import interfax.rest.client.QueryStringParameter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * [Sending Batches][Get Batch List].
 * 表題 REST API 呼び出し時に投入する Query String Parameter
 */
public class GetBatchListOptions implements QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Setter @Getter
    private Integer limit;
    
    @Setter @Getter
    private String lastId;
    
    @Setter @Getter
    private SortOrder sortOrder;
    
    @Setter @Getter
    private Boolean allUsers;
    
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
        if(allUsers != null) {
            params.add(new BasicNameValuePair("allUsers", String.valueOf(allUsers)));
        }
    }
}
