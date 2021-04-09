package main;

import interfax.rest.client.Connector;
import interfax.rest.client.sending.GetFaxListResponse;
import interfax.rest.client.sending.SearchFaxListOptions;
import interfax.rest.client.sending.SearchStatus;
import interfax.rest.client.sending.SendFaxStatus;
import interfax.rest.client.sending.SendingFaxes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] FAX送信の記録の検索.
 * 
 * 条件を指定して、FAX送信の記録を検索します。
 * [Sending Faxes]及び[Sending Batches]双方が検索対象となります。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxSearchFaxList {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());

            // send fax
            SendingFaxes sending = new SendingFaxes(conn);
            SearchFaxListOptions options = new SearchFaxListOptions();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);  // 当月1日を起点に指定
            options.setDateFrom(cal.getTime());
            options.setStatus(SearchStatus.All);
            //
            GetFaxListResponse response = sending.searchFaxList(options);
            if(response.isSuccess()) {
//                StringBuilder sb = new StringBuilder();
//                JsonString.format(sb, response.getBody());
//                System.out.println(sb.toString());
                
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d): %s\n", status.getStatusCode(), status.getReasonPhrase());
                for(SendFaxStatus faxStatus : response.getStatusList()) {
                    System.out.println("-----<detail level of [full]>-----");
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
                    System.out.printf("       [PageSize]: %s\n", faxStatus.getPageSize());
                    System.out.printf("[PageOrientation]: %s\n", faxStatus.getPageOrientation());
                    System.out.printf(" [PageResolution]: %s\n", faxStatus.getPageResolution());
                    System.out.printf("      [Rendering]: %s\n", faxStatus.getRendering());
                    System.out.printf("     [PageHeader]: %s\n", faxStatus.getPageHeader());
                    System.out.printf("     [SubmitTime]: %s\n", faxStatus.getSubmitTime());

                    System.out.printf("        [Subject]: %s\n", faxStatus.getSubject());
                    System.out.printf(" [DestinationFax]: %s\n", faxStatus.getDestinationFax());
                    System.out.printf("     [ReplyEmail]: %s\n", faxStatus.getReplyEmail());
                    System.out.printf(" [PagesSubmitted]: %d\n", faxStatus.getPagesSubmitted());
                    System.out.printf("     [SenderCSID]: %s\n", faxStatus.getSenderCSID());
                    System.out.printf("[AttemptsToPerform]: %d\n", faxStatus.getAttemptsToPerform());
                    System.out.printf("        [Contact]: %s\n", faxStatus.getContact());
                }
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxSearchFaxList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
