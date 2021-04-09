package main;

import interfax.rest.client.Connector;
import interfax.rest.client.MethodResponse;
import interfax.rest.client.upload.Disposition;
import interfax.rest.client.sending.FaxDestination;
import interfax.rest.client.sending.SendFaxOptions;
import interfax.rest.client.sending.SendFaxResponse;
import interfax.rest.client.sending.SendingFaxes;
import interfax.rest.client.upload.Sharing;
import interfax.rest.client.upload.UploadingDocuments;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
/**
 * [サンプルコード] 複数のアップロードコンテンツを使ったFAX送信.
 * 
 * 複数のFAXコンテンツを事前にアップロードし、FAX送信コンテンツとして
 * 利用します。尚、Text コンテンツをアップロードする場合、必ず UTF-8で
 * 無ければなりません。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecSendFaxWithUploadingDocumehts {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            do {
                Resource resource = Resource.get();
                Connector conn= new Connector(resource.getId(), resource.getPassword());
                
                // first
                UploadingDocuments firstUpload = new UploadingDocuments(conn);
                {
                    String content = "message-utf-8.txt";
                    URL url = ExecSendFaxWithUploadingDocumehts.class.getResource(content);
                    // create uploading session
                    Path file = Paths.get(url.toURI());
                    byte[] buff = Files.readAllBytes(file);
                    MethodResponse response = firstUpload.create(
                        content,
                        buff.length,
                        Disposition.SingleUse,
                        Sharing.Private);
                    if(!response.isSuccess()) break;
                    // upload chunk
                    ByteArrayInputStream in = new ByteArrayInputStream(buff);
                    response = firstUpload.uploadChunk(in);
                    if(!response.isSuccess()) break;
                }

                // second
                UploadingDocuments secondUpload = new UploadingDocuments(conn);
                {
                    String content = "word-カレンダー[2021].docx";
                    URL url = ExecSendFaxWithUploadingDocumehts.class.getResource(content);
                    // create uploading session
                    Path file = Paths.get(url.toURI());
                    byte[] buff = Files.readAllBytes(file);
                    MethodResponse response = secondUpload.create(
                        content,
                        buff.length,
                        Disposition.SingleUse,
                        Sharing.Private);
                    if(!response.isSuccess()) break;
                    // upload chunk
                    ByteArrayInputStream in = new ByteArrayInputStream(buff);
                    response = secondUpload.uploadChunk(in);
                    if(!response.isSuccess()) break;
                }

                // send fax
                SendingFaxes sending = new SendingFaxes(conn);
                SendFaxOptions options = new SendFaxOptions();
                options.setReplyAddress(resource.getEmail());
                SendFaxResponse response = sending.sendFax(
                    new FaxDestination("000", null),
                    Arrays.asList(firstUpload, secondUpload),
                    options);
                
                Response.StatusType status = response.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                if(response.isSuccess()) {
                    System.out.printf("[Access Status URL]: %s\n", response.getRequestResource());
                    System.out.printf("[Transaction Id]: %d\n", response.getTransactionId());
                } else {
                    System.out.printf("    [code]: %d\n", response.getCode());
                    System.out.printf(" [message]: %s\n", response.getMessage());
                    System.out.printf("[moreInfo]: %s\n", response.getMoreInfo());
                }
            }while(false);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecSendFaxWithUploadingDocumehts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
