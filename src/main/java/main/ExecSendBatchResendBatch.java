package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingBatches;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数宛先に対する一括 FAX送信(BATCH)の再送信.
 * 
 * Id を指定して、特定のBATCH 送信を再送信します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendBatchResendBatch {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            SendingBatches send = new SendingBatches(conn);
            SendFaxResponse response = send.resendBatch(0, "FAX再送", null); // <- 適切な Transaction Id を指定します
            //
            Response.StatusType status = response.getStatusType();
            System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            if(response.isSuccess()) {
                System.out.printf("      [Location]: %s\n", response.getRequestResource().toString());
                System.out.printf("[Transaction Id]: %d\n", response.getTransactionId());
            } else {
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecSendBatchResendBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
