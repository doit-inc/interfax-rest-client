package interfax.rest.client;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 */
public class JsonStringTest {
    
    ////////////////////////////////////////////////////////////////////////////
    // setup & finalize
    
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

    private final String assumption = 
        "{\n"
        + "  \"aaa\": 0,\n"
        + "  \"bbb\": [\n"
        + "    \"ddd\",\n"
        + "    \"eee\"\n"
        + "  ],\n"
        + "  \"fff\": {\n"
        + "    \"ggg\": \"hhh\"\n"
        + "  }\n"
        + "}";
    
    @Test
    public void testFormat() throws IOException {
        System.out.println("<<format>>");
        StringBuilder buffer = new StringBuilder();
        String json = "{\"aaa\":0, \"bbb\": [\"ddd\", \"eee\"], \"fff\":{\"ggg\":\"hhh\"}}";
        JsonString.format(buffer, json);
        // 
        assertEquals(assumption, buffer.toString());
    }
}
