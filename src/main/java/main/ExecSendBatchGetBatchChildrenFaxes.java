package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.GetFaxListResponse;
import interfax.rest.client.sending.SendFaxStatus;
import interfax.rest.client.sending.SendingBatches;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数宛先に対する一括 FAX送信(BATCH)の状態取得.
 * 
 * Transaction Id を指定し、発行済みのBATCH FAX送信をキャンセルします。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendBatchGetBatchChildrenFaxes {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());

            // send fax
            SendingBatches sending = new SendingBatches(conn);
            
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            GetFaxListResponse response = sending.getBatchChildFaxes(0, null, null); // <- Batch Transaction Id を指定する
            //
            Response.StatusType status = response.getStatusType();
            System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            if(response.isSuccess()) {
                for(SendFaxStatus faxStatus : response.getStatusList()) {
                    System.out.println("------------");
                    System.out.printf("             [Id]: %s\n", faxStatus.getId());
                    System.out.printf("            [URI]: %s\n", faxStatus.getUri());
                    System.out.printf("         [Status]: %d\n", faxStatus.getStatus());
                    System.out.printf("         [userId]: %s\n", faxStatus.getUserId());
                    System.out.printf("      [PagesSent]: %d\n", faxStatus.getPagesSent());
                    System.out.printf(" [CompletionTime]: %s\n", faxStatus.getCompletionTime());

                    System.out.printf("     [RemoteCSID]: %s\n", faxStatus.getRemoteCSID());
                    System.out.printf("       [Duration]: %d\n", faxStatus.getDuration());
                    System.out.printf("       [Priority]: %d\n", faxStatus.getPriority());
                    System.out.printf("          [Units]: %f\n", faxStatus.getUnits());
                    System.out.printf("    [CostPerUnit]: %f\n", faxStatus.getCostPerUnit());
                    System.out.printf("   [AttemptsMade]: %d\n", faxStatus.getAttemptsMade());
                }
            } else {
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendBatchGetBatchChildrenFaxes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
