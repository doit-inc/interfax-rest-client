package interfax.mime;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.HashMap;
import lombok.Getter;
/**
 * MIME Type の補足.
 * 
 * Files.probeContentType() から検索できる MIME Type を補足します。
 * PROTECTOON DOMAIN の META-INF/service/java.nio.file.spi.FileTypeDetector
 * によって、エントリーが行われています。
 * 
 * @see java.nio.file.Files
 */
public class ComplementFileTypeDetector extends FileTypeDetector {

    ////////////////////////////////////////////////////////////////////////////
    // inner class(s)
    
    public static enum TypeMap {
        
        ////////////////////////////////////////////////////////////////////////
        // enum(s)
        
        doc("application/msword"),
        docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        dot("application/msword"),
        xml("text/xml"),
        pdf("application/pdf"),
        tif("image/tiff"),
        txt("text/plain"),
        xls("application/vnd.ms-excel"),
        xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        htm("text/html"),
        html("text/html"),
        mht("message/rfc822"),
        url("application/internet-shortcut"),
        ppt("application/vnd.ms-powerpoint"),
        pptx("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        gif("image/gif"),
        png("image/png"),
        jpg("image/jpeg"),
        jpeg("image/jpeg"),
        ps("application/postscript"),
        eps("application/eps"),
        rtf("application/rtf"),
        bmp("image/bmp"),
        pcx("application/pcx"),
        snp("application/snapshot"),
        zip("application/zip");
        
        ////////////////////////////////////////////////////////////////////////
        // field(s)
        
        @Getter
        private final String mimeType;
        
        ////////////////////////////////////////////////////////////////////////
        // constructor
        
        private TypeMap(String mime) {
            mimeType = mime;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    private final HashMap<String, String> mimeTypeMao = new HashMap();

    ////////////////////////////////////////////////////////////////////////////
    // private  method(s)
    
    private void initialize() {
        for( TypeMap type : TypeMap.values() ) {
            mimeTypeMao.put(type.name(), type.getMimeType());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    @Override
    public String probeContentType(Path path) throws IOException {
        String result = null;
        String name = path.toString();
        int len = name.lastIndexOf(".");
        if(len != -1) {
            String ext = name.substring(len+1).toLowerCase();
            result = mimeTypeMao.get(ext);
        }
        return(result);
    }

    ////////////////////////////////////////////////////////////////////////////
    // constructor
    
    public ComplementFileTypeDetector() {
        this.initialize();
    }
}
