import java.nio.*;
import java.io.*;
import java.lang.*;

public class Ext2File{
	
	private ByteBuffer byteBuf;
	public Volume vol;
	
	public Ext2File(Volume vol){
	
		byteBuf = vol.getFile();
		this.vol = vol;
	}
	
	/**
	* Reads the given amount of bytes from the start of the file.
	* @param length the amount of bytes to read
	* @return byte[] bytes the byte array from the file system
	*/
	public byte[] read(long length) throws Exception{
		
		byte[] bytes = new byte[(int) length];
		
		System.arraycopy(vol.bytes, 1024, bytes, 0, 1024);
	
		return bytes;
	}

}
