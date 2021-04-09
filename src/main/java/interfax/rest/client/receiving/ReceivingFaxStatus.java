package interfax.rest.client.receiving;

import interfax.rest.client.DateDeserializer;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
/**
 * Recieved Fax List.
 * Response of [Receiving Faxes][Get List]
 * 
 * @see interfax.rest.client.receiving.GetListResponse
 * @see https://docs.uplandsoftware.com/interfax/documentation/api/rest/rest-api-reference/receiving-faxes/get-list/
 */
public class ReceivingFaxStatus {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private String userId;      // The username that received this transaction
    
    @Getter @Setter
    private long messageId;     // The internal ID assigned for this transaction
    
    @Getter @Setter
    private String phoneNumber; // The Phone number at which this fax was received
    
    @Getter @Setter
    private String remoteCSID;  // The CSID of the sender
    
    @Getter @Setter
    private int messageStatus;  // Status of the fax. See the list of InterFAX Error Codes
    
    @Getter @Setter
    private int pages;          // The number of pages received in this fax.
    
    @Getter @Setter
    private int messageSize;    // The size of the inbound fax image (in bytes)
    
    @Getter @Setter
    private int messageType;    // The type of message. Currently sends out static ‘1’.
    
    @Getter
    @XmlJavaTypeAdapter(DateDeserializer.class)
    private Date receiveTime;   // The time and date that the fax was received
                                // (formatted as dd/MM/yyyy hh:mm:ss). Times listed are GMT.
    @Getter @Setter
    private String callerId;    // The caller ID of the sender
    
    @Getter @Setter
    private int recordingDuration; // The time (in seconds) that it took to receive the fax
    
    @Getter @Setter
    private String imageStatus; // Indicates the status of the fax image – possible values are “UNREAD”, “READ” and “DONT_EXIST”
    
    @Getter @Setter
    private int numOfEmails;    // The number of email address that this fax should have been sent to once received
    
    @Getter @Setter
    private int numOfFailedEmails; //The number of email addresses that had failed delivery of the fax by email    
}
