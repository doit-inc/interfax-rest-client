package interfax.rest.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
/**
 * Hex Dump from InputStream.
 * InputStream を Filter し、通過データの Hex Dump を出力します。
 */
public class HexDumpInputStream  extends FilterInputStream {

    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    private final PrintStream out;
    //
    private int point = 0;
    private final byte[] buffer= new byte[16];
    // 
    private int address = 0;
    
    ////////////////////////////////////////////////////////////////////////////
    // private  method(s)
    
    private void flush() {
        if(point > 0) {
            StringBuilder sb = new StringBuilder();
            // address infor.
            sb.append(String.format("%08X: ", address));
            // hex dump
            for(int ix=0; (ix<point); ++ix) {
                sb.append(String.format("%02X", buffer[ix]));
                char sp = '.';
                if((ix % 8) == 7) sp = ' ';
                sb.append(sp);
                ++address;
            }
            int less = 58 - sb.length();
            while(less-- > 0) sb.append(' ');
            // raw data
            for(int ix=0; (ix<point); ++ix) {
                byte cc = buffer[ix];
                if( cc < 0x20 || cc > 0x7e) {
                    cc = ' ';
                }
                sb.append((char)cc);
            }
            point = 0;
            out.println(sb.toString());
        }
    }
    
    private void print(byte[] bytes, int offset, int length) {
        for(int ix=0; (ix<length); ++ix) {
            buffer[point++] = bytes[offset++];
            if(point >= 16) this.flush();
        }
        if(point >= 16) this.flush();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)
    
    @Override
    public int read() throws IOException {
        int result = super.read();
        if(result != -1) {
            this.print(new byte[]{(byte)(result & 0xFF)}, 0, 1);
        }
        return(result);
    }
    
    @Override
    public int read(byte[] buffer, int off, int len) throws IOException {
        int result = super.read(buffer, off, len);
        if(result != -1) {
            this.print(buffer, off, result);
        }
        return(result);
    }

    @Override
    public void close() throws IOException {
        this.flush();
        super.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    // constructor + static oinitializer

    /**
     * constructor.
     * 
     * @param base base input stream
     * @param print dump output stream
     */
    public HexDumpInputStream(InputStream base, PrintStream print) {
        super(base);
        this.out = print;
    }
}
