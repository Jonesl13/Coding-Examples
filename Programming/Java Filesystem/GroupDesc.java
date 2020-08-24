import java.nio.*;
import java.io.*;

public class GroupDesc{
	
	public Volume vol;
	private int n;
	
	public GroupDesc(Volume vol, int n){
		this.vol = vol;
		this.n = n;
	}
	
	/**
	 * Returns the byte pointer for the nth inode as integer
	 * @return int pointer
	 */
	public int iTablePointer(){
		return vol.byteBuf.getInt((2056)+(32*n));
	}
	

}
