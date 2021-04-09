package interfax.rest.client.sending;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodPath;
import interfax.rest.client.MethodResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.glassfish.jersey.message.internal.HttpHeaderReader;
/**
 * InterFAX REST.
 * [SendingFaxes] STUB
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/
 */
public class SendingFaxes {
    
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
     * [Sending Faxes][Send Fax].
     * 
     * @param destination
     * @param contents
     * @param options
     * @return MethodResponse
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @throws java.nio.charset.MalformedInputException 投入したテキストと
     *   Charset の情報が一致しない場合などにスローされます。
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/send-fax/
     */
    public SendFaxResponse sendFax(FaxDestination destination, List<FaxContent> contents, SendFaxOptions options ) throws URISyntaxException, IOException {
        SendFaxResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Send Fax]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        destination.addQueryString(params);
        if(options != null) options.addQueryString(params);
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_SendFax);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        
        // get FAX Contents
        Response response = null;
        Client client = m_connector.createClient();
        switch (contents.size()) {
            case 0:
                throw new IllegalArgumentException("Fax Content not Specified.");
            case 1:
                // single contents
                FaxContent first = contents.get(0);
                if(first instanceof URILinkContent) {
                    URILinkContent upload = (URILinkContent) first;
                    client.register((ClientRequestFilter) (ClientRequestContext requestContext) -> {
                        MultivaluedMap map = requestContext.getHeaders();
                        map.add("Content-Location", upload.getLinkURI().toString());
                    });
                    Entity<String> entity = Entity.entity("", upload.getMimeType());
                    WebTarget target = client.target(uri);
                    response = target.request().post(entity);
                } else if(first instanceof StreamContent) {
                    StreamContent stream = (StreamContent) first;
                    try (InputStream in = stream.open()) {
                        StringBuilder sb = new StringBuilder(stream.getMimeType());
                        InputStreamReader reader = null;
                        if(stream.isTextPlain() && stream.getCharset() != null) {
                            sb.append("; charset=UTF-8");
                            reader = new InputStreamReader(in, stream.getCharset().newDecoder());
                        }
                        if(reader != null) {
                            Entity<Reader> entity = Entity.entity(reader, sb.toString());
                            WebTarget target = client.target(uri);
                            response = target.request().post(entity);
                        } else {
                            Entity<InputStream> entity = Entity.entity(in, sb.toString());
                            WebTarget target = client.target(uri);
                            response = target.request().post(entity);
                        }
                    }
                }
                break;
            default:
                // multi contents
                ArrayList<Closeable> streamList = new ArrayList();
                MultiPart multiPart = new MultiPart();
                //
                try {
                    contents.forEach((FaxContent content) -> {
                        if(content instanceof URILinkContent) {
                            URILinkContent upload = (URILinkContent) content;
                            BodyPart body = new BodyPart(MediaType.TEXT_PLAIN_TYPE);
                            body.entity("");
                            MultivaluedMap<String, String> map = body.getHeaders();
                            map.add("Content-Location", upload.getLinkURI().toString());
                            multiPart.bodyPart(body);
                        } else if(content instanceof StreamContent) {
                            try {
                                StreamContent stream = (StreamContent) content;
                                StringBuilder sb = new StringBuilder(stream.getMimeType());
                                // Content InputStream
                                InputStream is = stream.open();
                                InputStreamReader streamReader;
                                if(stream.isTextPlain() && stream.getCharset() != null) {
                                    sb.append("; charset=UTF-8");
                                    streamReader = new InputStreamReader(is, stream.getCharset().newDecoder());
                                    is = new ReaderInputStream(streamReader, StandardCharsets.UTF_8);
                                }
                                streamList.add(is);

                                // Content Names
                                String heaerName = stream.getContentName();
                                String encodeName;
                                String checkName = URLEncoder.encode(heaerName, "UTF-8");
                                if(!heaerName.equals(checkName)) {
                                    encodeName = checkName;
                                    String plain = ".";
                                    int ix = heaerName.lastIndexOf(".");
                                    if(ix != -1) {
                                        plain = heaerName.substring(ix);
                                    }
                                    heaerName = plain;
                                } else {
                                    encodeName = null;
                                }
                                //
                                StreamDataBodyPart streamPart = new StreamDataBodyPart(
                                        heaerName,    // set to [Content-Disposition: name]
                                        is,
                                        heaerName,    // set to [Content-Disposition: filename]
                                        MediaType.valueOf(sb.toString())); // set [Content-Type:]
                                HttpHeaderReader reader = HttpHeaderReader.newInstance(
                                    streamPart.getFormDataContentDisposition().toString(), true);
                                FormDataContentDisposition fdd = new FormDataContentDisposition(reader, true) {
                                    @Override
                                    protected StringBuilder toStringBuffer() {
                                        StringBuilder sb = super.toStringBuffer();
                                        if(encodeName != null) {
                                            sb.append("; filename*=UTF-8''").append(encodeName);
                                        }
                                        return sb;
                                    }
                                };
                                streamPart.setFormDataContentDisposition(fdd);
                                multiPart.bodyPart(streamPart);
                            } catch (ParseException | UnsupportedEncodingException ex) {
                                Logger.getLogger(SendingFaxes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    // send fax request
                    Entity<MultiPart> entity = Entity.entity(multiPart, multiPart.getMediaType());
                    WebTarget target = client.target(uri);
                    response = target.request().post(entity);
                } finally {
                    streamList.forEach((in) -> {
                        try{in.close();}catch(IOException ex){}
                    });
                }
                break;
        }
        result = new SendFaxResponse(response);
        
        Response.StatusType status = result.getStatusType();
        m_logger.fine(
            String.format("[Sending faxes][Send Fax][Response] (%d) %s",
                status.getStatusCode(), status.getReasonPhrase()));
        m_logger.fine(
            String.format("[Sending faxes][Send Fax][Response][Body]: \n%s",
                result.getBody()));
        return(result);
    }

    /**
     * [Sending Faxes][Get Fax List].
     * 
     * @param options
     * @param ps (debug) HTTP Response Dump 取得用 stream、不要な場合は  nullを指定
     * @return GetFaxListResponse
     * @throws URISyntaxException 
     */
    public GetFaxListResponse getFaxList(GetFaxListOptions options, PrintStream ps) throws URISyntaxException {
        GetFaxListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Fax List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(options != null) options.addQueryString(params);
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_GetFaxList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetFaxListResponse(response, ps);
        return(result);
    }

    /**
     * [Sending Faxes][Get Fax List].
     * 
     * @param tids TransactionId list
     * @return GetFaxListResponse
     * @throws URISyntaxException 
     */
    public GetFaxListResponse getCompletedFaxList(List<Long> tids) throws URISyntaxException {
        GetFaxListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Fax List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        boolean start = true;
        StringBuilder sb = new StringBuilder();
        for(Long tid : tids) {
            if(start) start = false;
            else sb.append(",");
            sb.append(String.valueOf(tid));
        }
        params.add(new BasicNameValuePair("ids", sb.toString()));
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_GetCompletedFaxList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetFaxListResponse(response, null);
        return(result);
    }

    /**
     * [Sending Faxes][Get Fax List].
     * 
     * @param tid TransactionId
     * @return GetFaxListResponse
     * @throws URISyntaxException 
     */
    public GetFaxRecordResponse getFaxRecord(long tid) throws URISyntaxException {
        GetFaxRecordResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Fax List]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_GetFaxRecord, tid);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetFaxRecordResponse(response);
        return(result);
    }

    /**
     * [Sending Faxes][Get Image].
     * 
     * @param id Transaction Id
     * @param out Content OutputStream
     * @return MethodResponse
     * @throws URISyntaxException
     * @throws IOException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/get-fax-image/
     */
    public MethodResponse getFaxImage(long id, OutputStream out) throws URISyntaxException, IOException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Fax Image]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_GetFaxImage, id);
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
     * [Sending Faxes][Search Fax List].
     * 
     * @param options
     * @return GetFaxListResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/search-fax-list/
     */
    public GetFaxListResponse searchFaxList(SearchFaxListOptions options) throws URISyntaxException {
        GetFaxListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Search Fax List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(options != null) options.addQueryString(params);
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_SearchFaxList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetFaxListResponse(response, null);
        return(result);
    }
    
    /**
     * [Sending Faxes][CancelFax].
     * 
     * @param id
     * @return MethodResponse
     * @throws URISyntaxException
     * @throws IOException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/cancel-fax/
     */
    public MethodResponse cancelFax(long id) throws URISyntaxException, IOException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Cancel Fax]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_CancelFax, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Entity<String> entity = Entity.entity("", "text/html");
        Response response = target.request().post(entity);
        result = new MethodResponse(response);
        return(result);
    }

    /**
     * [Sending Faxes][Resend Fax].
     * 
     * @param id 再送する transactionId
     * @param faxNumber 再送先 Fax Number、 null の場合同一宛先への再送
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/resend-fax/
     */
    public SendFaxResponse resendFax(long id, String faxNumber) throws URISyntaxException {
        SendFaxResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Resend Fax]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(faxNumber != null)  params.add(new BasicNameValuePair("faxNumber", faxNumber));
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_ResendFax, id);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Entity<String> entity = Entity.entity("", "text/html");
        Response response = target.request().post(entity);
        result = new SendFaxResponse(response);
        return(result);
    }
    
    /**
     * [Sending Faxes][Hide Fax].
     * 
     * @param id
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/hide-fax/
     */
    public MethodResponse hideFax(long id) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Hide Fax]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingFaxes_HideFax, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Entity<String> entity = Entity.entity("", "text/html");
        Response response = target.request().post(entity);
        result = new MethodResponse(response);
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
 
    public SendingFaxes( Connector connector) {
        this.m_connector = connector;
    }
}
