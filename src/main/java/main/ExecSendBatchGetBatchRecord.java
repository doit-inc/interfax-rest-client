package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.GetBatchRecordResponse;
import interfax.rest.client.sending.SendBatchRecordStatus;
import interfax.rest.client.sending.SendingBatches;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数宛先に対する一括 FAX送信(BATCH)の状態取得.
 * 
 * Id を指定して、特定のBATCH 送信の結果を取得します。
 * 取得された状態には、処理結果件数が含まれます。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendBatchGetBatchRecord {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());

            // send fax
            SendingBatches sending = new SendingBatches(conn);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bao);
            GetBatchRecordResponse response = sending.getBatchRecord(0); // <- Batch Transaction Id を指定する
            //
            Response.StatusType status = response.getStatusType();
            System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            if(response.isSuccess()) {
                SendBatchRecordStatus faxStatus = response.getStatus();
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
                //
                System.out.printf("        [Total OK]: %d\n", faxStatus.getTotalOK());
                System.out.printf("    [Total Failed]: %d\n", faxStatus.getTotalFailed());
                System.out.printf("[Total In Process]: %d\n", faxStatus.getTotalInProcess());
            } else {
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendBatchGetBatchRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
