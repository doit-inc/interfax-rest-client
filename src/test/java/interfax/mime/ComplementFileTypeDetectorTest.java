package interfax.mime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 */
public class ComplementFileTypeDetectorTest {
    
    ////////////////////////////////////////////////////////////////////////////
    // start-up & finalize
    
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
    
    private final String[][] map = {
        {"doc","application/msword"},
        {"docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {"dot","application/msword"},
        {"xml","text/xml"},
        {"pdf","application/pdf"},
        {"tif","image/tiff"},
        {"txt","text/plain"},
        {"xls","application/vnd.ms-excel"},
        {"xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {"htm","text/html"},
        {"html","text/html"},
        {"mht","message/rfc822"},
        {"url","application/internet-shortcut"},
        {"ppt","application/vnd.ms-powerpoint"},
        {"pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        {"gif","image/gif"},
        {"png","image/png"},
        {"jpg","image/jpeg"},
        {"jpeg","image/jpeg"},
        {"ps","application/postscript"},
        {"eps","application/eps"},
        {"rtf","application/rtf"},
        {"bmp","image/bmp"},
        {"pcx","application/pcx"},
        {"snp","application/snapshot"},
        {"zip","application/zip"}
    };
    
    @Test
    public void testProbeContentType_static() throws Exception {
        System.out.println("<<probeContentType (static)>>");
        ComplementFileTypeDetector op = new ComplementFileTypeDetector();
        
        for(String[] set : map) {
            Path path = Paths.get("."+set[0]);
            assertEquals(set[1], op.probeContentType(path));
        }
    }
    
    @Test
    public void testProbeContentType_probe() throws Exception {
        System.out.println("<<probeContentType (probe)>>");
        
        for(String[] set : map) {
            Path path = Paths.get("."+set[0]);
            assertEquals(set[1], Files.probeContentType(path));
        }
    }
}
