package main;

import interfax.rest.client.Connector;
import interfax.rest.client.receiving.ForwardingEMail;
import interfax.rest.client.receiving.GetForwardingEMailsResponse;
import interfax.rest.client.receiving.ReceivingFaxes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 受信転送メールの送付先取得.
 * 特定のFAX受信を指定し、受信転送メールの送付記録を取得します。
 * 指定するId は、[Receiving Faxes][Get List] から取得される[MessageId]が該当します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecReceivingFaxesGetFoprwardingEMails {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // conn.setAccessMediaType(MediaType.TEXT_XML_TYPE);
            ReceivingFaxes recv = new ReceivingFaxes(conn);
            
            GetForwardingEMailsResponse response = recv.getForwardingEMails(0L); // <- 適切な ReceivingFaxStatus.MessageId を指定します
            if(response.isSuccess()) {
                for(ForwardingEMail mail : response.getForwardingEMailList()) {
                    System.out.println("-----");
                    System.out.printf(" [Message Status]: %d\n", mail.getMessageStatus());
                    System.out.printf("   [mail address]: %s\n", mail.getEmailAddress());
                    System.out.printf("[Completion Time]: %1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS %1$tZ\n", 
                                                                   mail.getCompletionTime());
                }
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecReceivingFaxesGetFoprwardingEMails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
