package interfax.rest.client.receiving;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodPath;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.sending.SendingFaxes;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
/**
 * InterFAX REST [Receiving Faxes].
 * [Receiving Faxes] へのアクセスに対する STUB
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/
 */
public class ReceivingFaxes {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    // Connector
    private final Connector m_connector;
    // Application Logger
    protected static final Logger m_logger = Logger.getLogger(SendingFaxes.class.getName());
    //
    private static final int BUFFER_SIZE = 8192;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * [Receiving Faxes][Get List].
     * 
     * @param options
     * @return GetListResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/get-list/
     */
    public GetListResponse getList(GetListOptions options) throws URISyntaxException {
        GetListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(options != null ) options.addQueryString(params);
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_GetList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetListResponse(response);
        
        return(result);
    }
    
    /**
     * [Receiving Faxes][Get Record].
     * 
     * @param id
     * @return 
     * @throws java.net.URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/get-record/
     */
    public GetRecordResponse getRecord(long id) throws URISyntaxException {
        GetRecordResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Record]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_GetRecord, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        result = new GetRecordResponse(response);
        
        return(result);
    }

    /**
     * [Receiving Faxes][Get Image].
     * 
     * @param id Recieved MessageId
     * @param out Content OutputStream
     * @return MethodResponse
     * @throws URISyntaxException
     * @throws IOException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/get-image/
     */
    public MethodResponse getImage(long id, OutputStream out) throws URISyntaxException, IOException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Image]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_GetLImage, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().get();
        result = new MethodResponse(response);
        if(result.isSuccess()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int ix;
            while ((ix = result.getStream().read(buffer)) > 0) {
                out.write(buffer, 0, ix);
            }
        }
        return(result);
    }
    
    /**
     * [Receiving Faxes][Get Forwarding EMails].
     * 
     * @param id Recieved MessageId
     * @return GetForwardingEMailsResponse
     * @throws URISyntaxException
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/get-forwarding-emails/
     */
    public GetForwardingEMailsResponse getForwardingEMails(long id) throws URISyntaxException {
        GetForwardingEMailsResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Forwarding EMails]");

        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_GetLForwardingEMails, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetForwardingEMailsResponse(response);
        
        return(result);
    }

    /**
     * [Receiving Faxes][Mark].
     * 
     * @param id - MessageId
     * @param mark true - 未読設定, false - 既読設定
     * @return MethodResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/mark/
     */
    public MethodResponse setMark(long id, boolean mark) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Mark]");

        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_Mark, id);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("unread", String.valueOf(mark)));
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().post(Entity.entity("", MediaType.TEXT_PLAIN_TYPE));
        result = new MethodResponse(response);
        
        return(result);
    }
    
    /**
     * [Receiving Faxes][Resend].
     * 
     * @param id
     * @param email
     * @return MethodResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/resend/
     */
    public MethodResponse resend(long id, String email) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Resend]");

        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.ReceivingFaxes_Resend, id);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("email", String.valueOf(email)));
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().post(Entity.entity("", MediaType.TEXT_PLAIN_TYPE));
        result = new MethodResponse(response);
        
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
 
    public ReceivingFaxes( Connector connector) {
        this.m_connector = connector;
    }
}
