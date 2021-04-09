package main;

import interfax.rest.client.Connector;
import interfax.rest.client.addressbooks.AddressBook;
import interfax.rest.client.addressbooks.AddressBooks;
import interfax.rest.client.addressbooks.GetAddressBookResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.StatusType;
/**
 * [サンプルコード] AddressBooks の取得.
 * [アカウント管理WEB]にて登録済みの [リスト](AddressBooks)の一覧を取得します。
 * 
 * 接続先サーバや InterFAX Id/Password は、Connector クラスで集約しています。
 * 当該サンプルでは Resource クラスを経由して、JSON で定義された外部リソース
 * を参照していますが、必要に応じて適宜変更してください。
 */
public class ExecGetAddressBooks {

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    public static void main(String[] argv) {
        try {
            Resource resource = Resource.get();
            Connector conn= new Connector(resource.getId(), resource.getPassword());
            // conn.setAccessMediaType(MediaType.TEXT_XML_TYPE);
            // get Address Book
            AddressBooks address = new AddressBooks(conn);
            GetAddressBookResponse result = address.getAddressBook(null);
            if(!result.isSuccess()) {
                StatusType status = result.getStatusType();
                System.out.printf("(%d) %s\n", status.getStatusCode(), status.getReasonPhrase());
                System.out.printf("    [code]: %d\n", result.getCode());
                System.out.printf(" [message]: %s\n", result.getMessage());
                System.out.printf("[moreInfo]: %s\n", result.getMoreInfo());
            } else {
                // System.out.println(result.getBody());
                for(AddressBook book : result.getAddressBooks() ) {
                    System.out.println("-----");
                    System.out.printf("[ContactListID]: %d\n", book.getContactListID());
                    System.out.printf("     [ListName]: %s\n", book.getListName());
                    System.out.printf("[NumOfContacts]: %d\n", book.getNumOfContacts());
                    System.out.printf("       [UserID]: %s\n", book.getUserID());
                    System.out.printf("    [isPrivate]: %b\n", book.isPrivate());
                }
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ExecGetAddressBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
