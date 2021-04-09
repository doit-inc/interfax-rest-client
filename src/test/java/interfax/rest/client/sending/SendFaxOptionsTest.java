package interfax.rest.client.sending;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 */
public class SendFaxOptionsTest {

    ////////////////////////////////////////////////////////////////////////////
    // setup + finalize
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // test method(s)

    @Test
    public void testSetPostponeTime() {
        System.out.println("<<setPostponeTime>>");
        Date postponeTime = new Date((2021-1900), (2-1), 22);
        SendFaxOptions op = new SendFaxOptions();
        op.setPostponeTime(postponeTime);
        List<NameValuePair> list = new ArrayList();
        op.addQueryString(list);
        assertEquals("postponeTime", list.get(0).getName());
        assertEquals("2021-02-22T00:00:00+09:00", list.get(0).getValue());
    }

    @Test
    public void testSetRendering() {
        System.out.println("<<setRendering>>");
        Rendering rendering = Rendering.Grayscale;
        SendFaxOptions op = new SendFaxOptions();
        op.setRendering(rendering);
        List<NameValuePair> list = new ArrayList();
        op.addQueryString(list);
        assertEquals("rendering", list.get(0).getName());
        assertEquals("Grayscale", list.get(0).getValue());
    }
}
