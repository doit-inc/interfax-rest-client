package interfax.rest.client.sending;

import java.net.URI;
/**
 * presentation of send fax contents.
 * URI による FAX送信コンテンツへのアクセス表現.
 */
public interface URILinkContent extends FaxContent {
    
    /////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public URI getLinkURI();
}
