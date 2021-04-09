package interfax.rest.client.sending;

import interfax.rest.client.QueryStringParameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * [Sending Faxes][Search Fax List].
 * 表題の REST API 呼び出し時に投入する Query Parameter
 */
public class SearchFaxListOptions implements QueryStringParameter {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    
    @Getter
    private final List<Long> ids = new ArrayList();
    @Setter
    private String reference;
    @Setter
    private Date dateFrom;
    @Setter
    private Date dateTo;
    @Setter
    private SearchStatus status;
    @Getter
    private final List<String> userId  = new ArrayList();
    @Setter
    private String faxNumber;
    @Setter
    private Integer limit;
    @Setter
    private Integer offset;

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    @Override
    public void addQueryString(List<NameValuePair> params) {
        if(ids.size() > 0) {
            boolean start = true;
            StringBuilder sb = new StringBuilder();
            for(Long tid : ids) {
                if(start) start = false;
                else sb.append(",");
                sb.append(String.valueOf(tid));
            }
            params.add(new BasicNameValuePair("ids", sb.toString()));
        }
        if(reference != null) {
            params.add(new BasicNameValuePair("reference", reference));
        }
        if( dateFrom != null) {
            params.add(new BasicNameValuePair("dateFrom", ISO8601.format(dateFrom)));
        }
        if( dateTo != null) {
            params.add(new BasicNameValuePair("dateTo", ISO8601.format(dateTo)));
        }
        if(status != null) {
            params.add(new BasicNameValuePair("status", status.name()));
        }
        if(userId.size() > 0) {
            boolean start = true;
            StringBuilder sb = new StringBuilder();
            for(String user : userId) {
                if(start) start = false;
                else sb.append(",");
                sb.append(user);
            }
            params.add(new BasicNameValuePair("userId", sb.toString()));
        }
        if( faxNumber != null) {
            params.add(new BasicNameValuePair("faxNumber", faxNumber));
        }
        if( limit != null) {
            params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }
        if( offset != null) {
            params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
    }
}
