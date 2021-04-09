package main;

import interfax.rest.client.Connector;
import interfax.rest.client.credit.GetOutboundCreditsResponse;
import interfax.rest.client.credit.OutboundCredits;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.StatusType;
/**
 * [サンプルコード] 与信残額の確認.
 * ユーザの与信残額を取得します。尚、この値は、[アカウント管理WEB]の
 * [アカウント管理]でも確認可能です。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecOutboundCredits {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // 
            OutboundCredits credit = new OutboundCredits(conn);
            GetOutboundCreditsResponse result = credit.getOutboundCredits();
            if(result.isSuccess()) {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setParseIntegerOnly(false);
                format.setMaximumFractionDigits(4);
                System.out.printf("[Remaining Faxing Credits]: %s\n", format.format(result.getCredit()));
            } else {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", result.getCode());
                System.out.printf(" [message]: %s\n", result.getMessage());
                System.out.printf("[moreInfo]: %s\n", result.getMoreInfo());
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecOutboundCredits.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
