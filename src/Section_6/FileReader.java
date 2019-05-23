package Section_6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileReader {
	ByteBuffer buf= ByteBuffer.allocate(65536);
    FileChannel channel;
    int readCount=0;
    
    public FileReader(String filename) throws FileNotFoundException {
        channel = new FileInputStream(filename).getChannel();
        buf.clear();
    }
    
    public int readIntFromText() throws IOException {
        int value = 0;
        while(true){
            if(readCount<=0){
                buf.clear();
                readCount = channel.read(buf);
                if(readCount<0){
                    break;
                }
                buf.flip();
            }
            byte nextChar = buf.get();
            readCount--;

            if(nextChar>='0' && nextChar<='9') {
                value = value * 10 + (nextChar - '0');
            }else{
                break;
            }
        }
        return value;
    }
}
