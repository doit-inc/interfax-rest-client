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
 * [サンプルコード] 複数のFAXコンテンツを使ったFAX送信.
 * 
 * 複数のFAXコンテンツを使ってFAX送信をリクエストします。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithMultiDocuments {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // conn.setTarget(Connector.Target.local);
            // conn.setTarget(Connector.Target.migration);

            // create FaxContent
            String firstContent = "message-cp932.txt";
            StreamContent first = new StreamContent(firstContent, () -> {
                return(ExecSendFaxWithMultiDocuments.class.getResourceAsStream(firstContent));
            }, Charset.forName("windows-31j"));
            //
            String secondContent = "excel-カレンダー[2021].xlsx";
            StreamContent second = new StreamContent(secondContent, () -> {
                return(ExecSendFaxWithMultiDocuments.class.getResourceAsStream(secondContent));
            }, null);

            // send fax
            SendingFaxes sending = new SendingFaxes(conn);
            SendFaxOptions options = new SendFaxOptions();
            options.setReplyAddress(resource.getEmail());
            //
            SendFaxResponse response = sending.sendFax(
                new FaxDestination("000", "鈴木(株)"),
                Arrays.asList(first, second), options);
            //
            Response.StatusType status = response.getStatusType();
            System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            if(response.isSuccess()) {
                System.out.printf("[Status Request Path]: %s\n", response.getRequestResource().toString());
                System.out.printf("[Transaction Id]: %d\n", response.getTransactionId());
            } else {
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxWithMultiDocuments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
