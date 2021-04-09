package interfax.rest.client;

import java.util.List;
import org.apache.http.NameValuePair;
/**
 * InterFAX REST.
 * 
 * 各 REST APIにおける Query String Parameter のセット表現です。
 * Query String Parameter   を利用する API は、それぞれ この 
 * interface を実装し、REST API 呼び出し時の Query String への
 * 投入メソッドを実装します。
 */
public interface QueryStringParameter {
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * Query String Parameter の投入。
     * 
     * @param params parameter の投入先
     */
    public void addQueryString(List<NameValuePair> params);
}
