package interfax.rest.client.sending;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import interfax.rest.client.QueryStringParameter;
/**
 * [Sending Faxes][Send Fax].
 * 表題 REST API 呼び出し時の Query String Parameter
 */
public class SendFaxOptions implements QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    public static enum FitToPage {
        scale ,
        noscale
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Setter @Getter
    private Date postponeTime;
    
    @Setter @Getter
    private Integer retriesToPerform;
    
    @Setter @Getter
    private String csid;
    
    @Setter @Getter
    private String pageHeader;
    
    @Setter @Getter
    private String reference;
    
    @Setter @Getter
    private String replyAddress;
    
    @Setter @Getter
    private PageSize pageSize;
    
    @Setter @Getter
    private FitToPage fitToPage;
    
    @Setter @Getter
    private PageOrientation pageOrientation;
    
    @Setter @Getter
    private Resolution resolution;
    
    @Setter @Getter
    private Rendering rendering;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * query string の追加設定.
     * @param params 追加先
     */
    @Override
    public void addQueryString(List<NameValuePair> params) {
        if(postponeTime != null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            params.add(new BasicNameValuePair("postponeTime", sf.format(postponeTime)));
        }
        if(retriesToPerform != null) {
            params.add(new BasicNameValuePair("retriesToPerform", String.valueOf(retriesToPerform)));
        }
        if(csid != null) {
            params.add(new BasicNameValuePair("csid", csid));
        }
        if(pageHeader != null) {
            params.add(new BasicNameValuePair("pageHeader", pageHeader));
        }
        if(reference != null) {
            params.add(new BasicNameValuePair("reference", reference));
        }
        if(replyAddress != null) {
            params.add(new BasicNameValuePair("replyAddress", replyAddress));
        }
        if(pageSize != null) {
            params.add(new BasicNameValuePair("pageSize", pageSize.name()));
        }
        if(fitToPage != null) {
            params.add(new BasicNameValuePair("fitToPage", fitToPage.name()));
        }
        if(pageOrientation != null) {
            params.add(new BasicNameValuePair("pageOrientation", pageOrientation.name()));
        }
        if(resolution != null) {
            params.add(new BasicNameValuePair("resolution", resolution.name()));
        }
        if(rendering != null) {
            params.add(new BasicNameValuePair("rendering", rendering.name()));
        }
    }
}
