package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.receiving.ReceivingFaxes;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] FAX受信イメージの取得.
 * 
 * FAX受信イメージを取得する為には、MessageId が必要です。
 * 同 Id は、[Receiving Faxes][Get List]から取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecReceivingFaxesGetImage {

    public static void main(String[] args) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            ReceivingFaxes recv = new ReceivingFaxes(conn);
            // 現実的には、テンポラリファイルとして作成してからリネームすべきですが....
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MethodResponse response = recv.getImage(0, os); // <- 適切な ReceivingFaxStatus.MessageId を指定します
            if(response.isSuccess()) {
                System.out.printf("[get content size]: %d\n", os.size());
                MultivaluedMap<String, String> headers = response.getHeaders();
                headers.keySet().forEach((val) -> {
                    System.out.printf("[(header) %s]: (first): %s\n", val, headers.getFirst(val));
                });
                if(headers.getFirst("Content-Type").equals("application/pdf")) {
                    File file = File.createTempFile("fax-content-", ".pdf");
                    try(FileOutputStream fo = new FileOutputStream(file)) {
                        fo.write(os.toByteArray());
                    }
                    System.out.printf("[Wrote Fax Content]: %s\n", file.getAbsolutePath());
                }
            } else {
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", response.getCode());
                System.out.printf(" [message]: %s\n", response.getMessage());
                System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ExecReceivingFaxesGetImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
