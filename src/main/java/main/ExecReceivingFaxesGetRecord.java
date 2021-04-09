package main;

import interfax.rest.client.Connector;
import interfax.rest.client.receiving.GetRecordResponse;
import interfax.rest.client.receiving.ReceivingFaxStatus;
import interfax.rest.client.receiving.ReceivingFaxes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] FAX受信情報の取得.
 * 
 * MessageId を指定し、特定の FAX受信に関するステータスを取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 * 
 * @deprecated usecase unknown
 */
public class ExecReceivingFaxesGetRecord {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            ReceivingFaxes recv = new ReceivingFaxes(conn);
            GetRecordResponse response = recv.getRecord(0); // <- 適切な ReceivingFaxStatus.MessageId を指定します
            if(response.isSuccess()) {
                ReceivingFaxStatus recvStatus = response.getReceivedFaxStatus();
                System.out.println("-----");
                System.out.printf("        [caller Id]: %s\n", recvStatus.getCallerId());
                System.out.printf("      [ImageStatus]: %s\n", recvStatus.getImageStatus());
                System.out.printf("        [MessageId]: %d\n", recvStatus.getMessageId());
                System.out.printf("      [MessageSize]: %d\n", recvStatus.getMessageSize());
                System.out.printf("    [MessageStatus]: %d\n", recvStatus.getMessageStatus());
                System.out.printf("      [MessageType]: %d\n", recvStatus.getMessageType());
                System.out.printf("      [NumOfEmails]: %d\n", recvStatus.getNumOfEmails());
                System.out.printf("[NumOfFailedEmails]: %d\n", recvStatus.getNumOfFailedEmails());
                System.out.printf("            [Pages]: %d\n", recvStatus.getPages());
                System.out.printf("      [PhoneNumber]: %s\n", recvStatus.getPhoneNumber());
                System.out.printf("      [PhoneNumber]: %1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS %1$tZ\n", 
                                                               recvStatus.getReceiveTime());
                System.out.printf("[RecordingDuration]: %d\n", recvStatus.getRecordingDuration());
                System.out.printf("       [RemoteCSID]: %s\n", recvStatus.getRemoteCSID());
                System.out.printf("           [UserId]: %s\n", recvStatus.getUserId());
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecReceivingFaxesGetRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
