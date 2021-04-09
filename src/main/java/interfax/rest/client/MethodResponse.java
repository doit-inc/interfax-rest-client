package interfax.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import lombok.Getter;
/**
 * InterFAX REST API Response.
 * 
 * InterFAX REST API 呼び出し時の、規定となるレスポンスクラスです。
 * API によってレスポンス情報が増加する場合、適宜、このクラスを拡張
 * します。
 */
public class MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    protected final Logger m_logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * HTTP Response Code
     */
    @Getter
    StatusType statusType;

    /**
     * HTTP Response Header
     */
    @Getter
    private MultivaluedMap<String, String> headers;

    /**
     * HTTP Response Body (raw)
     */
    private String m_body;
    
    @Getter
    private InputStream stream;
    
    /**
     * InterFAX REST API Response Code
     * @see https://rest.interfax.net/tests/errorcodes
     */
    @Getter
    private Integer code;

    /**
     * InterFAX REST API Response Message
     * @see https://rest.interfax.net/tests/errorcodes
     */
    @Getter
    private String message;

    /**
     * InterFAX REST API Response Message (More Information)
     * @see https://rest.interfax.net/tests/errorcodes
     */
    @Getter
    private String moreInfo;
    
    @Getter
    private final DateDeserializer dateDeserializer = new DateDeserializer();
    
    ////////////////////////////////////////////////////////////////////////////
    // private method(s)
    
    /**
     * エラー終了時の詳細をオブジェクト化.
     */
    private void importErrorResponse() {
        if(!this.isSuccess()) {
            String bodyContent = this.getBody();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            try {
                MethodResponse response = gson.fromJson(bodyContent, MethodResponse.class);
                if (response != null) {
                    this.code = response.code;
                    this.message = response.message;
                    this.moreInfo = response.moreInfo;
                }
            } catch (JsonSyntaxException ex) {
                m_logger.warning(String.format("[Response Code Phase Error]: %s", ex.getMessage()));
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    /**
     * API 呼出結果.
     * HTTP Response Code が 200番台の場合 true が返されます。
     * 
     * @return boolean true: api success
     */
    public final boolean isSuccess() {
        boolean result = false;
        if(statusType != null) {
            result = ((statusType.getStatusCode() / 100) == 2);
        }
        return(result);
    }

    /**
     * ヘッダの取得.
     * 指定ヘッダに含まれる最初の要素を取得します.
     * 
     * @param header
     * @return String 
     */
    public final String getHeaderFirst(String header) {
        return(this.headers.getFirst(header));
    }

    /**
     * Context-Type: ヘッダの最初の値を取得.
     * 
     * @return String Context-Type: header value
     */
    public String getContentType() {
        String result = this.getHeaderFirst("Content-Type");
        int i = result.indexOf(";");
        if( i != -1) {
            result = result.substring(0, i);
        }
        result = result.trim();
        result = result.toLowerCase();
        return(result);
    }

    /**
     * Content-Type が text/json か否か判定.
     * 
     * @return boolean TRUE: Content-Type == text/json
     */
    public final boolean isJsonContent() {
        return("text/json".equals(this.getContentType()));
    }

    /**
     * Content-Type が text/json か否か判定.
     * 
     * @return boolean TRUE: Content-Type == text/json
     */
    public final boolean isXMLContent() {
        return("text/xml".equals(this.getContentType()));
    }
    
    /**
     * Body Content 取得.
     * HTTP Response を String として取得します。
     * この機能は getStream() から復元されます。
     * その為、getStream() とは排他的な稼働になります。
     * 既に getStream() を取得しコンテンツを取得済みの場合、
     * String として Body を取得する事はできません。
     * 
     * @param ps dump output stream
     * @return String: body content
     */
    public final String getBody(PrintStream ps) {
        if(this.m_body == null) {
            InputStream std = this.getStream();
            if(ps != null) std = new HexDumpInputStream(std, ps);
            try(InputStream in = std){
                if( in.available() > 0) {
                    InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
                    Stream<String> streamOfString= new BufferedReader(inputStreamReader).lines();
                    this.m_body = streamOfString.collect(Collectors.joining());
                } else {
                    this.m_body = "";
                }
            } catch (IOException ex) {
                m_logger.warning(ex.getMessage());
                this.m_body = "";
            }
        }
        return(m_body);
    }
    
    /**
     * Body Content 取得.
     * HTTP Response を String として取得します。
     * この機能は getStream() から復元されます。
     * その為、getStream() とは排他的な稼働になります。
     * 既に getStream() を取得しコンテンツを取得済みの場合、
     * String として Body を取得する事はできません。
     * 
     * @return String: body content
     */
    public final String getBody() {
        return(this.getBody(null));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    /**
     * constructor.
     * 
     * @param response
     */
    public MethodResponse(Response response) {
        this.statusType = response.getStatusInfo();
        this.headers = response.getStringHeaders();
        this.stream = (InputStream) response.readEntity(InputStream.class);
        this.importErrorResponse();
    }
    
    // for JAXB
    protected MethodResponse() {
    }
}
