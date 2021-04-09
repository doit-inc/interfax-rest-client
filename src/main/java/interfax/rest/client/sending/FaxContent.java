package interfax.rest.client.sending;

import java.nio.charset.Charset;
/**
 * Fax Content Interface.
 * FAX送信をリクエストする際の、FAXコンテンツを表現します。
 */
public interface FaxContent {
    
    /////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * @return String: FAX Content Name
     */
    public String getContentName();
    
    /**
     * @return String: MIME Type
     */
    public String getMimeType();
    
    /**
     * テキスト送付時の charset.
     * 
     * @return Charset
     */
    public Charset getCharset();
    
    /**
     * @return boolean: true is "plain/text"
     */
    public default boolean isTextPlain() {
        String mime = this.getMimeType();
        return("text/plain".equals(mime));
    }
}
