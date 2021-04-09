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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
/**
 * [サンプルコード] XL形式によるFAX送信.
 * 
 * 特に指定が無い場合、Request/ Response は JSON 形式で行われます。
 * アプリケーションがそれらを意識する必要はありません。
 * ただし、XML形式でのやり取りを指示するそこも可能であり、このでは XML形式
 * を指定するケースのサンプルが記載されます。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithXMLMultiDestination {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            do {
                Resource resource = Resource.get();
                Connector conn= new Connector(resource.getId(), resource.getPassword());
                conn.setAccessMediaType(MediaType.TEXT_XML_TYPE);
                String content = "word-カレンダー[2021].docx";

                // create FaxContent
                StreamContent stream = new StreamContent(content, () -> {
                    return(ExecSendFaxWithXMLMultiDestination.class.getResourceAsStream(content));
                }, null);

                // send fax
                SendingBatches sending = new SendingBatches(conn);
                SendFaxOptions options = new SendFaxOptions();
                options.setReplyAddress(resource.getEmail());
                //
                SendFaxResponse response = sending.sendBatch(
                    Arrays.asList(
                        new FaxDestination("000", "鈴木(株)"),
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
            Logger.getLogger(ExecSendFaxWithXMLMultiDestination.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
