package interfax.rest.client.sending;

import java.net.URI;
import java.nio.charset.Charset;
import lombok.Getter;
/**
 * presentation of send fax contents.
 * URI による HTML の FAX送信コンテンツへのアクセス表現.
 */
public class URILinkDocument implements URILinkContent {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Getter
    private final String contentName = "linked.html";
    
    @Getter
    private final String mimeType = "text/html";
    
    @Getter
    private final Charset charset;
    
    @Getter
    private final URI linkURI;

    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public URILinkDocument(URI uri) {
        this.linkURI = uri;
        this.charset = null;
    }
}
