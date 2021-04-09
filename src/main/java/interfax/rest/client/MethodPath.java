package interfax.rest.client;

import lombok.Getter;
/**
 * InterFAX REST Service Path.
 */
public enum MethodPath {
    
    ////////////////////////////////////////////////////////////////////////////
    // enumeration
    
    // Address Books Method
    Addressbooks_GetAddressbooksList("/outbound/addressbooks"),
    // Fax Sending Method
    SendingFaxes_SendFax("/outbound/faxes"),
    SendingFaxes_GetFaxList("/outbound/faxes"),
    SendingFaxes_GetCompletedFaxList("/outbound/faxes/completed"),
    SendingFaxes_GetFaxRecord("/outbound/faxes/%d"),
    SendingFaxes_GetFaxImage("/outbound/faxes/%d/image"),
    SendingFaxes_SearchFaxList("/outbound/search"),
    SendingFaxes_CancelFax("/outbound/faxes/%d/cancel"),
    SendingFaxes_ResendFax("/outbound/faxes/%d/resend"),
    SendingFaxes_HideFax("/outbound/faxes/%d/hide"),
    SendingFaxes_GetSendingProfile("/outbound/sendingprofile"),
    SendingFaxes_GetContactList("/outbound/contacts/%s"),
    // Fax Sending Method (Batch)
    SendingBatches_SendingBatch("/outbound/batches"),
    SendingBatches_GetBatchList("/outbound/batches"),
    SendingBatches_GetBatchRecord("/outbound/batches/%d"),
    SendingBatches_GetBatchChildFaxes("/outbound/batches/%d/children"),
    SendingBatches_GetBatchImage("/outbound/batches/%d/image"),
    SendingBatches_ResendBatch("/outbound/batches/%d/resend"),
    SendingBatches_CancelBatch("/outbound/batches/%d/cancel"),
    SendingBatches_HideBatch("/outbound/batches/%d/hide"),
    // Uploading Fax Contents Method
    UploadingDocuments_Create("/outbound/documents"),
    UploadingDocuments_UploadChunk("/outbound/documents"),
    UploadingDocuments_GetList("/outbound/documents"),
    UploadingDocuments_GetStatus("/outbound/documents/%s"),
    UploadingDocuments_Cancel("/outbound/documents/%s"),
    // Fax Reciveing Method
    ReceivingFaxes_GetList("/inbound/faxes"),
    ReceivingFaxes_GetRecord("/inbound/faxes/%d"),
    ReceivingFaxes_GetLImage("/inbound/faxes/%d/image"),
    ReceivingFaxes_GetLForwardingEMails("/inbound/faxes/%d/emails"),
    ReceivingFaxes_Mark("/inbound/faxes/%d/mark"),
    ReceivingFaxes_Resend("/inbound/faxes/%d/resend"),
    // Get Outbound Credits
    GetOutboundCredits("/accounts/self/ppcards/balance");
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private final String Path;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    private MethodPath(String path) {
        this.Path = path;
    }
}
