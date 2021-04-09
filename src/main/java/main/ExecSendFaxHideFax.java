package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.sending.SendingFaxes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] FAX送信の状態情報の秘匿化.
 * 
 * TransactioId を指定して、特定のFAX送信の状態情報を秘匿化します。
 * 秘匿化さてた情報は、以降確認ができなくなります。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxHideFax {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            SendingFaxes send = new SendingFaxes(conn);
            MethodResponse response = send.hideFax(0); // <- 適切な Transaction Id を指定します
            //
            Response.StatusType status = response.getStatusType();
            System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            if(response.isSuccess()) {
                // NOP
            } else {
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecSendFaxHideFax.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
