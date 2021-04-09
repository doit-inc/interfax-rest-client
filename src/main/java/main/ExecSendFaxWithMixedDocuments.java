package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.upload.Disposition;
import interfax.rest.client.sending.FaxDestination;
import interfax.rest.client.sending.SendFaxOptions;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingFaxes;
import interfax.rest.client.upload.Sharing;
import interfax.rest.client.sending.StreamContent;
import interfax.rest.client.upload.UploadingDocuments;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] テキスト及び事前アップロードコンテンツによるFAX送信.
 * 
 * 2つのFAXコンテンツを使ってFAX送信をリクエストします。
 * 尚、比較的大きなサイズのコンテンツは、[Uploading Documents]
 * を使って事前にアップロードします。また、テキストは、文字コードを指定して
 * リクエストします。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithMixedDocuments {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            do {
                Resource resource = Resource.get();
                Connector conn= new Connector(resource.getId(), resource.getPassword());

                // text content
                String content = "message-cp932.txt";
                StreamContent stream = new StreamContent(content, () -> {
                    return(ExecSendFaxWithSingleText.class.getResourceAsStream(content));
                }, Charset.forName("windows-31j"));
                
                // upload content
                UploadingDocuments upload = new UploadingDocuments(conn);
                String content2 = "powerpoint-カレンダー[2021]-02.pptx";
                URL url = ExecSendFaxWithMixedDocuments.class.getResource(content2);
                // create iploading session
                Path file = Paths.get(url.toURI());
                byte[] buff = Files.readAllBytes(file);
                MethodResponse response = upload.create(content2, buff.length, Disposition.SingleUse, Sharing.Shared);
                if(!response.isSuccess()) break;
                // upload chunk
                ByteArrayInputStream in = new ByteArrayInputStream(buff);
                response = upload.uploadChunk(in);
                if(!response.isSuccess()) break;

                // send fax
                SendingFaxes sending = new SendingFaxes(conn);
                SendFaxOptions options = new SendFaxOptions();
                options.setReplyAddress("fujioka@do-it.co.jp");
                SendFaxResponse sendResponse = sending.sendFax(new FaxDestination("000", null), Arrays.asList(stream, upload), options);
                Response.StatusType status = sendResponse.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                if(sendResponse.isSuccess()) {
                    System.out.printf("[Status Request Path]: %s\n", sendResponse.getRequestResource().toString());
                    System.out.printf("[Transaction Id]: %d\n", sendResponse.getTransactionId());
                } else {
                    System.out.printf("    [code]: %d\n", sendResponse.getCode());
                    System.out.printf(" [message]: %s\n", sendResponse.getMessage());
                    System.out.printf("[moreInfo]: %s\n", sendResponse.getMoreInfo());
                }
            }while(false);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxWithMixedDocuments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
