package interfax.rest.client.addressbooks;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodPath;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
/**
 * InterFAX REST [Address Book].
 * [Sending Faxes][Sending Batches][Addressbooks] へのアクセスに対する STUB
 */
public class AddressBooks {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    // Connector
    private final Connector m_connector;
    // Application Logger
    protected static final Logger m_logger = Logger.getLogger(AddressBooks.class.getName());
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * Get Address Books.
     * 
     * @param options
     * @return GetAddressBookResponse
     * @throws URISyntaxException 
     */
    public GetAddressBookResponse getAddressBook(GetAddressListOptions options) throws URISyntaxException {
        GetAddressBookResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Addressbooks List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(options != null ) options.addQueryString(params);
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.Addressbooks_GetAddressbooksList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetAddressBookResponse(response);
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
 
    public AddressBooks( Connector connector) {
        this.m_connector = connector;
    }
}
