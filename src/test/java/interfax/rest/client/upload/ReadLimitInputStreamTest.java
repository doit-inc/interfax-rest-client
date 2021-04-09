package interfax.rest.client.upload;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 */
public class ReadLimitInputStreamTest {

    ////////////////////////////////////////////////////////////////////////////
    // setup
    
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
    public void testAvailable() throws Exception {
        System.out.println("<<available>>");
        ByteArrayInputStream bytes = new ByteArrayInputStream("ABCDEFGH".getBytes());
        ReadLimitInputStream op = new ReadLimitInputStream(bytes, 3);
        assertEquals(3, op.available());
        op.read();
        assertEquals(2, op.available());
        op.addLimit(5);
        assertEquals(7, op.available());
    }

    @Test
    public void testRead_0args() throws Exception {
        System.out.println("<<read()>>");
        ByteArrayInputStream bytes = new ByteArrayInputStream("ABCDEFGH".getBytes());
        ReadLimitInputStream op = new ReadLimitInputStream(bytes, 3);
        assertEquals('A',  op.read());
        assertEquals('B',  op.read());
        assertEquals('C',  op.read());
        assertEquals(-1,  op.read());
        //
        op.addLimit(3);
        assertEquals('D',  op.read());
        assertEquals('E',  op.read());
        assertEquals('F',  op.read());
        assertEquals(-1,  op.read());
        //
        op.addLimit(3);
        assertEquals('G',  op.read());
        assertEquals('H',  op.read());
        assertEquals(-1,  op.read());
    }

    @Test
    public void testRead_3args() throws Exception {
        System.out.println("<<read(byte[], int, int)>>");
        ByteArrayInputStream bytes = new ByteArrayInputStream("ABCDEFGH".getBytes());
        ReadLimitInputStream op = new ReadLimitInputStream(bytes, 3);
        
        byte[] buff = new byte[5];
        int len = op.read(buff, 0, buff.length);
        assertEquals(3,  len);
        assertTrue(Arrays.equals(new byte[]{'A', 'B', 'C', 0, 0}, buff));
        op.addLimit(5);
        len = op.read(buff, 0, buff.length);
        assertEquals(5,  len);
        assertTrue(Arrays.equals(new byte[]{'D', 'E', 'F', 'G', 'H'}, buff));
    }
}
