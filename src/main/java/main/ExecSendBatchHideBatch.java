package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.sending.SendingBatches;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数宛先に対する一括 FAX送信(BATCH)の秘匿化.
 * 
 * Id を指定して、特定のBATCH 送信を秘匿化します。
 * 秘匿化さてた BATCHは、以降確認ができなくなります。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendBatchHideBatch {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            SendingBatches send = new SendingBatches(conn);
            MethodResponse response = send.hideBatch(0); // <- 適切な Transaction Id を指定します
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
            Logger.getLogger(ExecSendBatchHideBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
