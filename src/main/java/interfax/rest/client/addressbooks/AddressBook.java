package interfax.rest.client.addressbooks;

import lombok.Getter;
import lombok.Setter;
/**
 * InterFAX REST [Address Book].
 * [Addressbooks][Get Addressbooks List] で返される情報のクラス宣言
 * 
 * @see interfax.rest.client.addressbooks.GetAddressBookResponse
 */
public class AddressBook {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter @Setter
    private long ContactListID;
    
    @Getter @Setter
    private boolean isPrivate;
    
    @Getter @Setter
    private String listName;
    
    @Getter @Setter
    private String userID;
    
    @Getter @Setter
    private long numOfContacts;
}
