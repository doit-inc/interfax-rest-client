package interfax.rest.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 */
public class HexDumpInputStreamTest {

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
    public void testRead() throws Exception {
        System.out.println("<<read>>");
        byte[] randomData = new byte[250];
        Random random = new Random();
        random.nextBytes(randomData);
        ByteArrayInputStream bai = new ByteArrayInputStream(randomData);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bao);
        try(HexDumpInputStream op = new HexDumpInputStream(bai, ps)) {
            byte[] buffer = new byte[32];
            while(true) {
                if( op.read() == -1 ) break;
                if( op.read(buffer, 0, 7) == -1 ) break;
                if( op.read(buffer) == -1 ) break;
            }
        }
        System.out.println(new String(bao.toByteArray()));
    }
}
