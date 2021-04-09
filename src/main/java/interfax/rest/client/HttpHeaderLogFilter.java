package interfax.rest.client;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
/**
 * REST Filter - logging.
 * 
 * javax.ws.rs.client.Client.regist() を使って事前登録する事で、
 * REST 呼出の際のヘッダをロギングします。
 */
public class HttpHeaderLogFilter implements ClientRequestFilter, ClientResponseFilter {
    
    ////////////////////////////////////////////////////////////////////////////
    // private method(s)
    
    private void log(URI uri, MultivaluedMap<String, ?> headers) {
        System.out.println("[URI]: " + uri.getPath());
        System.out.println("[Headers]:");
        headers.entrySet().forEach(h -> {
            String key = h.getKey();
            String value = h.getValue().toString();
            if("Authorization".equalsIgnoreCase(key)) {
                value = "(.. Confidentiality ..)";
            }
            System.out.printf("%s: %s\n", key, value);
        });
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    @Override
    public void filter(ClientRequestContext reqContext) throws IOException {
        System.out.println("<< Request Headers >>");
        log(reqContext.getUri(), reqContext.getHeaders());
    }

    @Override
    public void filter(ClientRequestContext reqContext, ClientResponseContext resContext) throws IOException {
        System.out.println("<< Response Headers >>");
        log(reqContext.getUri(), resContext.getHeaders());
    }
}
