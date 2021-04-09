package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.receiving.ReceivingFaxes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
/**
 * [サンプルコード] FAX受信情報の既読/未読マーキング.
 * 
 * MessageId を指定し、特定の FAX受信に関し既読マーク設定します。
 * 同マークは、[Receiving Faxes][Get List]を呼び出す際にオプション
 * として指定できる情報です。
 * 
 * マーキングを設定する為には、MessageId が必要です。
 * 同 Id は、[Receiving Faxes][Get List]から取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecReceivingFaxesMark {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            ReceivingFaxes recv = new ReceivingFaxes(conn);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MethodResponse response = recv.setMark(0, true); // <- 適切な ReceivingFaxStatus.MessageId を指定します
            if(response.isSuccess()) {
                StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecReceivingFaxesMark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
