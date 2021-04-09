package interfax.rest.client.sending;

import interfax.rest.client.MethodResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import lombok.Getter;
/**
 * [Sending Faxes][Send Fax].
 * 表題の API呼出で返される値の MethodResponse 拡張.
 */
public class SendFaxResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)

    // get transaction Id
    private static final Pattern GET_TRANSACTION_ID_PATTERN = Pattern.compile("^.*/(\\p{Digit}+)$");
    
    @Getter
    private URI requestResource;
    
    @Getter
    private Long transactionId;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public SendFaxResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            try {
                this.requestResource = new URI(this.getHeaderFirst("Location"));
                Matcher match = GET_TRANSACTION_ID_PATTERN.matcher(this.requestResource.toString());
                if(match.find()) {
                    this.transactionId = Long.parseLong(match.group(1));
                }
            } catch (URISyntaxException ex) {
                m_logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
