package interfax.rest.client;

import java.util.HashMap;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.URIBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
/**
 * InterFAX REST Service Connector.
 * 
 * InterFAX RESET Service に対する、基本の接続情報を維持します。
 * 
 * @see interfax.rest.client.addressbooks.AddressBook
 * @see interfax.rest.client.receiving.ReceivingFaxes
 * @see interfax.rest.client.sending.SendingBatches
 * @see interfax.rest.client.sending.SendingFaxes
 * @see interfax.rest.client.sending.UploadingDocuments
 */
public class Connector {
    
    ////////////////////////////////////////////////////////////////////////////
    // inner class(es)
    
    public static enum Target {
        official,
        migration,
        local
    }
    
    public static class AccessInfo {
        @Getter
        private final String host;
        @Getter
        private final String scheme;
        @Getter
        private final int port;
        //
        public AccessInfo(String host, String scheme, int port) {
            this.host = host;
            this.scheme = scheme;
            this.port = port;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    // Connection Timeout (mili.sec)
    private int connect_timeout = 15*1000;
    // read Timeout (mili.sec)
    private int read_timeout = 15*1000;
    // Access User Id
    @Getter
    private final String userId;
    // HTTP Authentication Attribute
    private final HttpAuthenticationFeature authentication;
    // Header Logger
    private final HttpHeaderLogFilter headerLog = new HttpHeaderLogFilter();
    // access server
    @Setter @Getter
    private Target target = Target.official;
    // access server list
    private final HashMap<Target, AccessInfo> accessMap = new HashMap(){
        {put(Target.official, new AccessInfo("rest.interfax.net","https",443));}
        // ローカルの http サーバを使い、投入した HTTP Request Header の確認に利用します
        {put(Target.local, new AccessInfo("localhost","http",8080));}
    };
    // service access interface media type
    @Getter
    private MediaType accessMediaType = null;
    @Getter
    private URIBuilder latestReleaseURI;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    /**
     * REST Service リクエスト時の型を指定.
     * REST Service呼び出し時のフォームを指定します。
     * 指定可能なのは下記の2つです。
     * <pre>
     * MediaType.APPLICATION_JSON_TYPE
     * MediaType.TEXT_XML_TYPE
     * </pre>
     * @param type MediaType
     * @return 設定前の値
     * @see IllegalArgumentException 規定外の値が指定された
     */
    public MediaType setAccessMediaType(MediaType type) {
        MediaType result = this.accessMediaType;
        do {
            if( type == null) break;
            if( type == MediaType.TEXT_XML_TYPE) break;
            if( type == MediaType.APPLICATION_JSON_TYPE) break;
            throw new IllegalArgumentException(String.format("%s is not supported.", type.toString()));
        }while(false);
        this.accessMediaType = type; 
        return(result);
    }
    
    /**
     * Client Request Entry の作成.
     * 
     * @return Client
     */
    public Client createClient() {
        ClientConfig configuration = new ClientConfig();
        configuration.property(ClientProperties.CONNECT_TIMEOUT, connect_timeout);
        configuration.property(ClientProperties.READ_TIMEOUT, read_timeout);

        Client result = ClientBuilder.newClient(configuration);
        result.register(headerLog)
                .register(authentication)
                .register(MultiPartFeature.class)
                .register(RequestEntityProcessing.CHUNKED)
                .register(JacksonFeature.class);
        return(result);
    }

    /**
     * REST Request URL の取得.
     * Query String は別途追記する必要があります。
     * 
     * @param path service offset path
     * @param argv Path 調整用の動的値
     * @return URIBuilder
     */
    public URIBuilder getURIBuilder(MethodPath path, Object... argv) {
        latestReleaseURI = new URIBuilder();
        AccessInfo info = accessMap.get(target);
        latestReleaseURI.setHost(info.getHost())
                .setScheme(info.getScheme())
                .setPort(info.getPort())
                .setPath(String.format(path.getPath(), argv));
        return(latestReleaseURI);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer

    /**
     * constructor.
     * @param id
     * @param password 
     */
    public Connector( String id, String password) {
        this.userId = id;
        authentication = HttpAuthenticationFeature.basic(id, password);
    }
    
    /**
     * constructor.
     * @param id
     * @param password
     * @param connctTimeout
     * @param readTimeout 
     */
    public Connector( String id, String password, int connctTimeout, int readTimeout) {
        this.userId = id;
        authentication = HttpAuthenticationFeature.basic(id, password);
        this.connect_timeout = connctTimeout;
        this.read_timeout = readTimeout;
    }
}
