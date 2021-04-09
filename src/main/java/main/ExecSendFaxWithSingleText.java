package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.FaxDestination;
import interfax.rest.client.sending.SendFaxOptions;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingFaxes;
import interfax.rest.client.sending.StreamContent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 単一の Text コンテンツを使ったFAX送信.
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithSingleText {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            String content = "message-cp932.txt";

            // create FaxContent
            StreamContent stream = new StreamContent(content, () -> {
                return(ExecSendFaxWithSingleText.class.getResourceAsStream(content));
            }, Charset.forName("windows-31j"));

            // send fax
            SendingFaxes sending = new SendingFaxes(conn);
            SendFaxOptions options = new SendFaxOptions();
            options.setReplyAddress(resource.getEmail());
            SendFaxResponse response = sending.sendFax(new FaxDestination("000", "佐藤(株)"), Arrays.asList(stream), options);
            if(response.isSuccess()) {
                System.out.printf("[Access Status URL]: %s\n", response.getRequestResource());
                System.out.printf("[Transaction Id]: %d\n", response.getTransactionId());
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxWithSingleText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
