package interfax.rest.client.sending;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
/**
 * presentation of send fax contents.
 * InputStream による FAX送信コンテンツへのアクセス表現.
 */
public class StreamContent  implements FaxContent {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    @Getter
    private String contentName;
    
    @Getter
    private String mimeType;
    
    @Getter
    private final Charset charset;
    
    private final Supplier<InputStream> opener;
    
    ////////////////////////////////////////////////////////////////////////////
    // private method(s)
    
    private void initializer(String  contentName) {
        try {
            this.contentName = contentName;
            // mime type
            Path path = Paths.get(contentName);
            mimeType = Files.probeContentType(path);
        } catch (IOException ex) {
            Logger.getLogger(StreamContent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public InputStream open() {
        return(this.opener.get());
    }

    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    /**
     * constructor.
     * 
     * @param contentName
     * @param opener InputStream opener
     * @param charset コンテンツが Text の 場合、文字セットを指定します。
     */
    public StreamContent(String contentName, Supplier opener, Charset charset) {
        this.opener = opener;
        this.charset = charset;
        this.initializer(contentName);
    }
}
