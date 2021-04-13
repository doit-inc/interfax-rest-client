package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.FaxDestination;
import interfax.rest.client.sending.URILinkDocument;
import interfax.rest.client.sending.SendFaxOptions;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingFaxes;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 外部の HTML ページを使ったFAX送信.
 * 
 * InterFAXサービスから動的にアクセスが可能な HTMLページを指定して
 * FAX送信をリクエストします。尚、セキュリティ上の理由から、参照される
 * HTMLページでの JavaScript の機能は無効化されます。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithSingleHtmlLink {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            do {
                Resource resource = Resource.get();
                Connector conn= new Connector(resource.getId(), resource.getPassword());
                URILinkDocument html = new URILinkDocument(
                    new URI("http://www.do-it.co.jp/main/aboutus/aboutus.html"));

                // send fax
                SendingFaxes sending = new SendingFaxes(conn);
                SendFaxOptions options = new SendFaxOptions();
                options.setReplyAddress(resource.getEmail());
                SendFaxResponse response = sending.sendFax(new FaxDestination("000", null), Arrays.asList(html), options);
                //
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                if(response.isSuccess()) {
                    System.out.printf("[Access Status URL]: %s\n", response.getRequestResource());
                    System.out.printf("[Transaction Id]: %d\n", response.getTransactionId());
                } else {
                    System.out.printf("    [code]: %d\n", response.getCode());
                    System.out.printf(" [message]: %s\n", response.getMessage());
                    System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
                }
            }while(false);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxWithSingleHtmlLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
