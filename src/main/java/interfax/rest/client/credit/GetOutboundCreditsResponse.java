package interfax.rest.client.credit;

import interfax.rest.client.MethodResponse;
import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import lombok.Getter;
/**
 * [Get Outbound Credits]
 */
public class GetOutboundCreditsResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private BigDecimal credit;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    public GetOutboundCreditsResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            credit = new BigDecimal(this.getBody());
        }
    }
}
