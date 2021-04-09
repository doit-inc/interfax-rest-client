package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.GetBatchListOptions;
import interfax.rest.client.sending.GetBatchListResponse;
import interfax.rest.client.sending.SendBatchStatus;
import interfax.rest.client.sending.SendingBatches;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数宛先に対する一括 FAX送信(BATCH)の一覧取得.
 * 
 * 実施済み BATCH 送信の一覧を取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendBatchGetBatchList {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());

            // send fax
            SendingBatches sending = new SendingBatches(conn);
            int max = 100;
            
            GetBatchListOptions options = new GetBatchListOptions();
            options.setLimit(15);
            while(true) {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(bao);
                GetBatchListResponse response = sending.getBatchList(options);
                //
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                if(response.isSuccess()) {
                    for(SendBatchStatus faxStatus : response.getStatusList()) {
                        System.out.println("------------");
                        System.out.printf("  [Batch Head Id]: %s\n", faxStatus.getBatchHeadId());
                        System.out.printf("         [userId]: %s\n", faxStatus.getUserId());
                        System.out.printf("[numOfTransactions]: %d\n", faxStatus.getNumOfTransactions());
                        System.out.printf("     [SubmitTime]: %s\n", faxStatus.getSubmitTime());
                        System.out.printf("     [ReplyEmail]: %s\n", faxStatus.getReplyEmail());
                        System.out.printf("      [reference]: %s\n", faxStatus.getReference());
                        System.out.printf(" [PagesSubmitted]: %d\n", faxStatus.getPagesSubmitted());
                        System.out.printf("     [SenderCSID]: %s\n", faxStatus.getSenderCSID());
                        System.out.printf("       [PageSize]: %s\n", faxStatus.getPageSize());
                        System.out.printf("[PageOrientation]: %s\n", faxStatus.getPageOrientation());
                        System.out.printf(" [PageResolution]: %s\n", faxStatus.getPageResolution());
                        System.out.printf("[renderingQuality]: %s\n", faxStatus.getRenderingQuality());
                        System.out.printf("     [PageHeader]: %s\n", faxStatus.getPageHeader());
                        System.out.printf("[AttemptsToPerform]: %d\n", faxStatus.getAttemptsToPerform());
                    }
                } else {
                    System.out.printf("    [code]: %d\n", response.getCode());
                    System.out.printf(" [message]: %s\n", response.getMessage());
                    System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
                    break;
                }
                // System.out.printf("[body]: \n%s\n", response.getBody());
                //
                List<SendBatchStatus> statusList = response.getStatusList();
                int size = statusList.size();
                if(size <=0 ) break;
                //
                max -= size;
                if(max <= 0) break;
                // 次の読出し位置を設定
                options.setLastId(statusList.get(size-1).getBatchHeadId());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendBatchGetBatchList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
