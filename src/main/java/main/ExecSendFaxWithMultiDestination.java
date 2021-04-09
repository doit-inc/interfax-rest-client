package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.FaxDestination;
import interfax.rest.client.sending.SendFaxOptions;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingBatches;
import interfax.rest.client.sending.StreamContent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
/**
 * [サンプルコード] 複数宛先指定 (BATCH) のFAX送信.
 * 
 * 単一リクエストで複数のFAX送付先を指定したFAX送信リクエストを発行します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithMultiDestination {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            do {
                Resource resource = Resource.get();
                Connector conn= new Connector(resource.getId(), resource.getPassword());
                String content = "word-カレンダー[2021].docx";

                // create FaxContent
                StreamContent stream = new StreamContent(content, () -> {
                    return(ExecSendFaxWithMultiDestination.class.getResourceAsStream(content));
                }, null);

                // send fax
                SendingBatches sending = new SendingBatches(conn);
                SendFaxOptions options = new SendFaxOptions();
                options.setReplyAddress(resource.getEmail());
                //
                SendFaxResponse response = sending.sendBatch(
                    Arrays.asList(
                        new FaxDestination("000", "鈴木(株)"),  // FAX番号と宛名を指定します
                        new FaxDestination("000", "佐藤(株)")),
                    Arrays.asList(stream),
                    options);
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
            }while(false);
        } catch (URISyntaxException | IOException | JAXBException ex) {
            Logger.getLogger(ExecSendFaxWithMultiDestination.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
