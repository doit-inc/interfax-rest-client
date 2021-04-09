package interfax.rest.client.sending;

import interfax.rest.client.DateDeserializer;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
/**
 * [Sending Batches][Get Batch List].
 * 表題の API呼出で返される値の表現.
 */
public class SendBatchStatus  {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private String batchHeadId;
    @Getter @Setter
    private String userId;
    @Getter @Setter
    private long numOfTransactions;
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date submitTime;
    @Getter @Setter
    private String replyEmail;
    @Getter @Setter
    private String reference;
    @Getter @Setter
    private int pagesSubmitted;
    @Getter @Setter
    private String senderCSID;
    @Getter @Setter
    private PageSize pageSize;
    @Getter @Setter
    private PageOrientation pageOrientation;
    @Getter @Setter
    private Rendering renderingQuality;
    @Getter @Setter
    private Resolution pageResolution;
    @Getter @Setter
    private String pageHeader;
    @Getter @Setter
    private int attemptsToPerform;
}
