package interfax.rest.client.credit;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodPath;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.addressbooks.AddressBooks;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.http.client.utils.URIBuilder;

/**
 * [Get Outbound Credits]
 */
public class OutboundCredits {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    // Connector
    private final Connector m_connector;
    // Application Logger
    protected static final Logger m_logger = Logger.getLogger(AddressBooks.class.getName());
    
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * [OutboundCredits]
     * 
     * @return
     * @throws URISyntaxException 
     */
    public GetOutboundCreditsResponse getOutboundCredits() throws URISyntaxException {
        GetOutboundCreditsResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Outbound Credits]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.GetOutboundCredits);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        result = new GetOutboundCreditsResponse(response);
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
 
    public OutboundCredits( Connector connector) {
        this.m_connector = connector;
    }
}
