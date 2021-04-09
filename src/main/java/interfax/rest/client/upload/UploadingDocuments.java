package interfax.rest.client.upload;

import interfax.rest.client.MethodPath;
import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.sending.URILinkContent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import lombok.Getter;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
/**
 * presentation of send fax contents.
 * [Uploading Documents] にって事前投入済みの FAX送信コンテンツへのアクセス表現.
 * 
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/
 */
public class UploadingDocuments implements URILinkContent {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    private static final int MAX_CHUNK_SIZE = 1024*1024;
    
    // Connector
    private final Connector m_connector;
    // Application Logger
    protected static final Logger m_logger = Logger.getLogger(UploadingDocuments.class.getName());

    @Getter
    private String contentName;
    @Getter
    private int contentSize;
    @Getter
    private Charset charset;
    @Getter
    private String mimeType;
    @Getter
    private URI linkURI;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * create uploading document session.
     * 
     * [Sending Faxes][Uploading Documents] の為のセッションを開きます。
     * セッションオープンのタイミングで、同時にコンテンツの投入はできません。
     * また、Text を投入する場合、charset == UTF-8 と仮定されます。
     * 
     * @param contentName
     * @param size upload content size
     * @param disposition
     * @param sharing 
     * @return  MethodResponse
     * @throws java.io.IOException 
     * @throws java.net.URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/create/
     */
    public MethodResponse create(
            String contentName,
            int size,
            Disposition disposition,
            Sharing sharing
    ) throws IOException, URISyntaxException {
        MethodResponse result;
        
        m_logger.entering(this.getClass().getName(), "[Create]");
        this.contentName = contentName;
        this.contentSize = size;
        
        // mime type
        Path path = Paths.get(contentName);
        mimeType = Files.probeContentType(path);
        
        // URL Query Strings
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("size", String.valueOf(size)));
        params.add(new BasicNameValuePair("name", contentName));
        params.add(new BasicNameValuePair("disposition", disposition.name()));
        params.add(new BasicNameValuePair("sharing", sharing.getDefine()));
        // URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.UploadingDocuments_Create);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        m_logger.info(String.format(
            "[Uploading Documents][Create][URI]: %s", uri.toString()));
        
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        
        StringBuilder sb = new StringBuilder(mimeType);
        if(this.mimeType.equals("text/plain")) {
            sb.append("; charset=UTF-8");
            this.charset = StandardCharsets.UTF_8;
        }
        Entity<String> entity = Entity.entity("", sb.toString());
        
        Response response = target.request().post(entity);
        result = new MethodResponse(response);
        
        m_logger.info(String.format("[Sending faxes][Send Fax][Response] (%d) %s",
                result.getStatusType().getStatusCode(), result.getStatusType().getReasonPhrase()));
        // get location
        String location = response.getHeaderString("Location");
        if(location != null) {
            linkURI = new URI(location);
        }
        m_logger.info(String.format(
            "[Uploading Documents][Create][Response][Location]: %s", location));
        m_logger.info(String.format(
            "[Uploading Documents][Create][Response][Body]: \n%s",result.getBody()));
        return(result);
    }
    
    /**
     * upload chunk.
     * Fax コンテンツを事前投入します。
     * 予め、create() を呼び出し、uploading session を開く必要があります。
     * 
     * @param contents upload contents
     * @return MethodResponse
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/upload-chunk/
     */
    public MethodResponse uploadChunk(InputStream contents) {
        MethodResponse result = null;

        m_logger.entering(this.getClass().getName(), "[Upload Chunk]");
        m_logger.info(String.format(
            "[Uploading Documents][Upload Chunk][URI]: %s", linkURI.toString()));
        
        Client client = m_connector.createClient();
        WebTarget target = client.target(linkURI);

        ReadLimitInputStream limitStream = new ReadLimitInputStream(contents, MAX_CHUNK_SIZE);
        for(int start=0; (start < this.contentSize); ) {
            int stop = start + MAX_CHUNK_SIZE - 1;
            if(stop >= this.contentSize)  {
                stop = this.contentSize - 1;
            }
            //
            Builder builder = target.request();
            builder.header("Range", String.format("bytes=%d-%d", start, stop));
            // range
            Entity<InputStream> entity = Entity.entity(limitStream, mimeType);
            Response response = builder.post(entity);
            result = new MethodResponse(response);
            //
            StatusType status = response.getStatusInfo();
            m_logger.info(
                String.format("[Uploading Documents][Upload Chunk][Response] (%d) %s",
                    status.getStatusCode(), status.getReasonPhrase()));
            m_logger.info(
                String.format("[Uploading Documents][Upload Chunk][Response][Body]: \n%s",
                    result.getBody()));
            m_logger.info(
                String.format("[Uploading Documents][Upload Chunk][Response][Header][X-Content-Range]: %s\n",
                    result.getHeaderFirst("X-Content-Range")));
            if(!result.isSuccess()) break;
            //
            start = stop +1;
            limitStream.addLimit(MAX_CHUNK_SIZE);
        }
        return(result);
    }
    
    /**
     * [Uploading Documents][Get List].
     * 
     * @param limit 
     * @param offset
     * @return GetListResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/get-list/
     */
    public GetListResponse getList(Integer limit, Integer offset) throws URISyntaxException {
        GetListResponse result;
        m_logger.entering(this.getClass().getName(), "[Get List]");
        
        // URL Query Strings
        List<NameValuePair> params = new ArrayList();
        if(limit != null)   params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        if(offset != null)  params.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        // URL
        URIBuilder uriBuilder = m_connector.getURIBuilder(MethodPath.UploadingDocuments_GetList);
        uriBuilder.setParameters(params);
        URI uri = uriBuilder.build();
        
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
     * [Uploading Documents][Get List].
     * 
     * @param uri [Create] 若しくは [Get List] で取得した URI
     * @return GetStatusResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/get-status/
     */
    public GetStatusResponse getStatus(URI uri) throws URISyntaxException {
        GetStatusResponse result;
        m_logger.entering(this.getClass().getName(), "[Get Status]");

        // URL
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Builder builder = target.request();
        MediaType type = m_connector.getAccessMediaType();
        if(type != null) builder.accept(type);
        Response response = builder.get();
        result = new GetStatusResponse(response);
        
        return(result);
    }
    
    /**
     * [Uploading Documents][Cancel].
     * 
     * @param uri [Create] 若しくは [Get List] で取得した URI
     * @return MethodResponse
     * @throws URISyntaxException 
     * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/sending-faxes/uploading-documents/cancel/
     */
    public MethodResponse cancel(URI uri) throws URISyntaxException {
        MethodResponse result;
        m_logger.entering(this.getClass().getName(), "[Cancel]");

        // URL
        Client client = m_connector.createClient();
        WebTarget target = client.target(uri);
        Response response = target.request().delete();
        result = new MethodResponse(response);
        
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
 
    public UploadingDocuments( Connector connector) {
        this.m_connector = connector;
    }
}
