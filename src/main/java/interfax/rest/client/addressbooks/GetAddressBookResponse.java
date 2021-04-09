package interfax.rest.client.addressbooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import interfax.rest.client.MethodResponse;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
/**
 * InterFAX REST [Address Book].
 * [Addressbooks][Get Addressbooks List] で返される情報のクラス宣言
 */
@XmlRootElement(name="ArrayOfContactListHead")
public class GetAddressBookResponse extends MethodResponse {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    @XmlElement(name="ContactListHead")
    private List<AddressBook> addressBooks;
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public GetAddressBookResponse(Response response) {
        super(response);
        if(this.isSuccess()) {
            if(this.isJsonContent()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<AddressBook>>(){}.getType();
                this.addressBooks = gson.fromJson(this.getBody(), collectionType);
            } else if(this.isXMLContent()) {
                StringReader reader = new StringReader(this.getBody());
                GetAddressBookResponse fromXML = JAXB.unmarshal(reader, GetAddressBookResponse.class);
                this.addressBooks = fromXML.getAddressBooks();
            }
        }
    }

    // for JAXB
    private GetAddressBookResponse() {
    }
}
