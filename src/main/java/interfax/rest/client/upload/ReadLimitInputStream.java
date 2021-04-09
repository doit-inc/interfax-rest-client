package interfax.rest.client.upload;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.Getter;
/**
 * 読出し制限付き InputStream.
 * 
 * パラメータとしての投入時に、読出し上限値を指定して利用します。
 * 上限値を更新する事でインスタンスを継続して利用できます。
 */
public class ReadLimitInputStream extends FilterInputStream {
    
    ////////////////////////////////////////////////////////////////////////////
    // field(s)
    
    @Getter
    private long limit;
    
    @Getter
    private long loadedSize = 0;
    
    ////////////////////////////////////////////////////////////////////////////
    // public method(s)

    /**
     * 読出し上限値の更新.
     * 
     * @param limit 既存の上限値に加算する値
     * @return 
     */
    public long addLimit(long limit) {
        long result = this.limit;
        this.limit += limit;
        return(result);
    }
    
    @Override
    public boolean markSupported() {
        return(false);
    }

    @Override
    public int available() throws IOException {
        int result = 0;
        if(this.loadedSize < this.limit) {
            int real = super.available();
            result = (int)(this.limit - this.loadedSize);
            if(real < result) result = real;
        }
        return(result);
    }

    @Override
    public int read() throws IOException {
        int result = -1;
        if(this.loadedSize < this.limit) {
            result = super.read();
            if(result != -1 ) {
                ++this.loadedSize;
            }
        }
        return(result);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int result = -1;
        if(this.loadedSize < this.limit) {
            if((this.loadedSize+len) > this.limit) {
                len = (int)(limit - this.loadedSize);
            }
            result = super.read(b, off, len);
            if(result != -1 ) this.loadedSize += result;
        }
        return(result);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // constructor + static initializer
    
    public ReadLimitInputStream(InputStream in, long limit) {
        super(in);
        this.limit = limit;
    }
}
