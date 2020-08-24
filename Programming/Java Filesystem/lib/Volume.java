import java.nio.*;
import java.io.*;

public class Volume{
	
	public RandomAccessFile randFile;
	public ByteBuffer byteBuf;
	public byte[] bytes;
	
	/**
	* Reads the given ext2file and converts it into a ByteBuffer file 
	* @param String filename
	*/
	public Volume(String filename)throws Exception{
			
			randFile = new RandomAccessFile(filename,"r");	
			
			int size = (int)randFile.length();
			
			bytes = new byte[size];
			randFile.read(bytes,0,size);
			byteBuf = ByteBuffer.wrap(bytes);
			byteBuf.order(ByteOrder.LITTLE_ENDIAN);
			
			
	}
	
	/**
	* Gets the ByteBuffer file
	* @return ByteBuffer byteBuf
	*/
	public ByteBuffer getFile(){
		
		return byteBuf;
	}
}
