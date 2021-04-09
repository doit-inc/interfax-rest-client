package interfax.rest.client.sending;

import java.io.Writer;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * [Sending Batches][Send Batch].
 * 表題 REST API 呼び出し時における、list の XML表現.
 */
@XmlRootElement(name="ArrayOfDestination")
public class XmlFaxDestination {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @XmlElement(name="Destination")
    private List<FaxDestination> list = null;

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    /**
     * XML 取得.
     * List&lt;FaxDestination&gt; の XML イメージを取得します。
     * ただし、white space は削除されます。
     * 
     * @param writer xml image の出力先
     * @throws JAXBException 
     */
    public void write(Writer writer) throws JAXBException {
        JAXBContext contextObj = JAXBContext.newInstance(this.getClass());
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshallerObj.marshal(this, writer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // constractor + static initializer
    
    public XmlFaxDestination(List<FaxDestination> faxList) {
        this.list = faxList;
    }

    // for JAXB
    private XmlFaxDestination() {
    }
}
