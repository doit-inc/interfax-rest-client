package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.GetFaxListResponse;
import interfax.rest.client.sending.SendFaxStatus;
import interfax.rest.client.sending.SendingFaxes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 完了済み FAX送信の状態取得.
 * 
 * Id を指定して、完了済みFAX送信の情報を取得します。
 * id は複数指定できます。完了していないFAX送信がリクエストされた場合は無視されます。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxGetCompletedFaxList {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn = new Connector(resource.getId(), resource.getPassword());

            // send fax
            SendingFaxes sending = new SendingFaxes(conn);
            
            // 検索対象の TransactionId を準備
            List<Long> tids = new ArrayList() {
                {add(0L);}
                {add(1L);}
            };
            
            GetFaxListResponse response = sending.getCompletedFaxList(tids);
            if (response.isSuccess()) {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d): %s\n", status.getStatusCode(), status.getReasonPhrase());
                for (SendFaxStatus faxStatus : response.getStatusList()) {
                    System.out.println("-----<detail level of [summary]>-------");
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
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxGetCompletedFaxList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
