package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.upload.UploadingDocuments;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.StatusType;
/**
 * [サンプルコード] アップロード済みFAX送信コンテンツを破棄.
 * 
 * [Uploading Documents][Create] で取得した、或いは
 * [Uploading Documents][Get List]で取得した URI を指定して、
 * アップロード済みFAX送信コンテンツを破棄します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecUploadingCancel {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            UploadingDocuments upload = new UploadingDocuments(conn);
            // [Uploading Documents][Create] で、或いは [Uploading Documents][Get List]で取得した URI
            URI uri = new URI("https://rest.interfax.net/outbound/documents/{uri}");
            MethodResponse result = upload.cancel(uri);
            if(result.isSuccess()) {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
            } else {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", result.getCode());
                System.out.printf(" [message]: %s\n", result.getMessage());
                System.out.printf("[moreInfo]: %s\n", result.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecUploadingCancel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
