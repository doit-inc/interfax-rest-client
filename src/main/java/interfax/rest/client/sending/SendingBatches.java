package interfax.rest.client.sending;

import com.google.gson.Gson;
import interfax.rest.client.Connector;
import interfax.rest.client.MethodPath;
import interfax.rest.client.MethodResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
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
import javax.xml.bind.JAXBException;
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
 * [Sending Batches] STUB
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/
 */
public class SendingBatches {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    // Connector
    private final Connector m_connector;
    // Application Logger
    protected static final Logger m_logger = Logger.getLogger(SendingBatches.class.getName());
    //
    private static final int BUFFER_SIZE = 8192;
    
    ////////////////////////////////////////////////////////////////////////////
    // private method(s)
    
    /**
     * submit [Sending Batches][Send Batch].
     * 
     * @param params Query String Parameter
     * @param contents Fax Contents
     * @return
     * @throws URISyntaxException
     * @throws IOException 
     * @throws java.nio.charset.MalformedInputException 投入したテキストと
     *   Charset の情報が一致しない場合などにスローされます。
     */
    private SendFaxResponse submitBatch(List<NameValuePair> params, List<FaxContent> contents) throws URISyntaxException, IOException {
        SendFaxResponse result;
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_SendingBatch);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        
        // eet FAX Contents
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
                    contents.forEach((content) -> {
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
            String.format("[Send Batches][Send Batch][Response] (%d) %s",
                status.getStatusCode(), status.getReasonPhrase()));
        m_logger.fine(
            String.format("[Send Batches][Send Batch][Response][Body]: \n%s",
                result.getBody()));
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * [Sending Batches][Send Batch].
     * Connector の AccessMediaType に依存して、List&lt;FaxDestination&gt; の
     * query staring が XML 形式で記述されるか JSON 形式で記述されるかが判断されます。
     * 
     * @param destinationList
     * @param contents
     * @param options
     * @return MethodResponse
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @throws javax.xml.bind.JAXBException
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/send-fax/
     */
    public SendFaxResponse sendBatch(List<FaxDestination> destinationList, List<FaxContent> contents, SendFaxOptions options ) throws URISyntaxException, IOException, JAXBException {
        SendFaxResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Send Batch]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        MediaType type = this.m_connector.getAccessMediaType();
        if(type != null && type == MediaType.TEXT_XML_TYPE) {
            StringWriter sw = new StringWriter();
            XmlFaxDestination xml = new XmlFaxDestination(destinationList);
            xml.write(sw);
            params.add(new BasicNameValuePair("list", sw.toString()));
        } else {
            Gson gson = new Gson();
            params.add(new BasicNameValuePair("list", gson.toJson(destinationList)));
        }
        if(options != null) options.addQueryString(params);
        
        result = this.submitBatch(params, contents);
        return(result);
    }
    
    /**
     * * Send Fax Request with Address Book.
     * 
     * @param addresBookName
     * @param contents
     * @param options
     * @return MethodResponse
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/send-fax/
     */
    public SendFaxResponse sendBatch(String addresBookName, List<FaxContent> contents, SendFaxOptions options ) throws URISyntaxException, IOException {
        SendFaxResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Send Batch]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        Gson gson = new Gson();
        params.add(new BasicNameValuePair("list", addresBookName));
        if(options != null) options.addQueryString(params);
        
        result = this.submitBatch(params, contents);
        return(result);
    }
    
    /**
     * [Sending Batches][Get Batch List]
     * 
     * @param options Query String Options
     * @return GetBatchListResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/get-batch-list/
     */
    public GetBatchListResponse getBatchList(GetBatchListOptions options) throws URISyntaxException {
        GetBatchListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Batch List]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(options != null) options.addQueryString(params);
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_GetBatchList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetBatchListResponse(response);
        return(result);
    }
    
    /**
     * [Sending Batches][Get Batch Record]
     * 
     * @param id search transaction id
     * @return GetBatchRecordResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/get-batch-record/
     */
    public GetBatchRecordResponse getBatchRecord(long id) throws URISyntaxException {
        GetBatchRecordResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Batch Record]");
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_GetBatchRecord, id);
        URI uri = uriBuilder.build();
        //
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetBatchRecordResponse(response);
        return(result);
    }
    
    /**
     * [Sending Batches][Get Batch Child Faxes]
     * 
     * @param id
     * @param limit
     * @param offset
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/get-batch-child-faxes/
     */
    public GetFaxListResponse getBatchChildFaxes(long id, Integer limit, Integer offset ) throws URISyntaxException {
        GetFaxListResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Batch Child Faxes]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(limit != null)  params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        if(offset != null) params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_GetBatchChildFaxes, id);
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
     * [Sending Batches][Get Batch Image].
     * 
     * @param id transactionId
     * @param out Get Image Output Stream
     * @return MethodResponse
     * @throws URISyntaxException
     * @throws IOException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/get-batch-image/
     */
    public MethodResponse getBatchImage(long id, OutputStream out) throws URISyntaxException, IOException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Get Batch Image]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_GetBatchImage, id);
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
     * [Sending Batches][Resend Batch]
     * 
     * @param id Resend TransactionId
     * @param reference
     * @param sendAlsoOK
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/resend-batch/
     */
    public SendFaxResponse resendBatch(long id, String reference, Boolean sendAlsoOK) throws URISyntaxException {
        SendFaxResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Resend Batch]");
        
        // URL Query String
        List<NameValuePair> params = new ArrayList();
        if(reference != null)  params.add(new BasicNameValuePair("reference", reference));
        if(sendAlsoOK != null) params.add(new BasicNameValuePair("sendAlsoOK", String.valueOf(sendAlsoOK)));
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_ResendBatch, id);
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
     * [Sending Batches][Cancel Batch].
     * 
     * @param id
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/cancel-batch/
     */
    public MethodResponse cancelBatch(long id) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Cancel Batch]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_CancelBatch, id);
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
     * [Sending Batches][Hide Batch].
     * 
     * @param id
     * @return
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/sending-batches/hide-batch/
     */
    public MethodResponse hideBatch(long id) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getSimpleName(), "[Cancel Batch]");
        
        // Request URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.SendingBatches_HideBatch, id);
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
 
    public SendingBatches( Connector connector) {
        this.m_connector = connector;
    }
}
