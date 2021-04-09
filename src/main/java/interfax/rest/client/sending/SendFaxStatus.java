package interfax.rest.client.sending;

import interfax.rest.client.DateDeserializer;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
/**
 * sent fax transaction status.
 * 
 * @see [Sending faxes][GetFaxList]
 * @see [Sending faxes][Get Completed Fax List]
 */
public class SendFaxStatus  {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private String id;
    @Getter @Setter
    private URI uri;
    @Getter @Setter
    private int status;
    @Getter @Setter
    private String userId;
    @Getter @Setter
    private int pagesSent;
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date completionTime;
    @Getter @Setter
    private String remoteCSID;
    @Getter @Setter
    private int duration;
    @Getter @Setter
    private int priority;
    @Getter @Setter
    private double units;
    @Getter @Setter
    private double costPerUnit;
    @Getter @Setter
    private int attemptsMade;
    @Getter @Setter
    private PageSize pageSize;
    @Getter @Setter
    private PageOrientation pageOrientation;
    @Getter @Setter
    private Resolution pageResolution;
    @Getter @Setter
    private Rendering rendering;
    @Getter @Setter
    private String pageHeader;
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date submitTime;
    @Getter @Setter
    private String subject;
    @Getter @Setter
    private String destinationFax;
    @Getter @Setter
    private String replyEmail;
    @Getter @Setter
    private int pagesSubmitted;
    @Getter @Setter
    private String senderCSID;
    @Getter @Setter
    private int attemptsToPerform;
    @Getter @Setter
    private String contact;
}
