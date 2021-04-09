package main;

import interfax.rest.client.Connector;
import interfax.rest.client.upload.GetStatusResponse;
import interfax.rest.client.upload.UploadedContentStatus;
import interfax.rest.client.upload.UploadingDocuments;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.StatusType;
/**
 * [サンプルコード] アップロード済みFAX送信コンテンツの状態取得.
 * 
 * [Uploading Documents][Create] で取得した、或いは
 * [Uploading Documents][Get List]で取得した URI を指定して、
 * アップロード済みFAX送信コンテンツの状態を取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecUploadingGetStatus {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            UploadingDocuments upload = new UploadingDocuments(conn);
            // URI には、 [Create] で取得した、或いは [Get List]で取得した URI を投入します
            URI uri = new URI("https://rest.interfax.net/outbound/documents/{id}");
            GetStatusResponse result = upload.getStatus(uri);
            if(result.isSuccess()) {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());

                UploadedContentStatus one = result.getStatus();
                System.out.println("-----");
                System.out.printf("       [userId]: %s\n", one.getUserId());
                System.out.printf("     [fileName]: %s\n", one.getFileName());
                System.out.printf("     [fileSize]: %d\n", one.getFileSize());
                System.out.printf("    [up@loaded]: %d\n", one.getUploaded());
                System.out.printf("          [URI]: %s\n", one.getUri());
                System.out.printf(" [creationTime]: %1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS %1$tZ\n", one.getCreationTime());
                System.out.printf("[lastUsageTime]: %1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS %1$tZ\n", one.getLastusageTime());
                System.out.printf("       [status]: %s\n", one.getStatus().name());
                System.out.printf("  [disposition]: %s\n", one.getDisposition().name());
                System.out.printf("      [sharing]: %s\n", one.getSharing().name());
            } else {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", result.getCode());
                System.out.printf(" [message]: %s\n", result.getMessage());
                System.out.printf("[moreInfo]: %s\n", result.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecUploadingGetStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
