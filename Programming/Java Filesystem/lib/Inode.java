import java.nio.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class Inode{
	
	public Volume vol;
	public int pointer;
	
	public Inode(Volume vol, SuperBlock sB, int start, int inodeNumber)throws Exception{
		this.vol = vol;
		
		pointer = start;

	}
	/**
	 * Scans the given Inode space for a File Mode indicator
	 * @return String filemode describing the functions of the Inode
	 */
	public String fileMode(){
		short temp = vol.byteBuf.getShort(pointer);
		int code = temp & 0xffff;
		
		String fileMode = new String();
		
		if ((code & 0xc000) == 0xc00){
			fileMode += "s";
			
		}else if ((code & 0xa000) == 0xa000){
			fileMode += "l";
			
		}else if ((code & 0x8000) == 0x8000){
			fileMode += "-";
		
		}else if ((code & 0x6000) == 0x6000){
			fileMode += "b";
		
		}else if ((code & 0x4000) == 0x4000){
			fileMode += "d";
		
		}else if ((code & 0x2000) == 0x2000){
			fileMode += "c";
		
		}else if ((code & 0x1000) == 0x1000){
			fileMode += "p";
		
		}
		
		if((code & 0x0100) == 0x0100){
			fileMode += "r";
		} else {
			fileMode += "-";
		}
		
		if((code & 0x0080) == 0x0080){
			fileMode += "w";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0040) == 0x0040){
			fileMode += "x";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0020) == 0x0020){
			fileMode += "r";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0010) == 0x0010){
			fileMode += "w";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0008) == 0x0008){
			fileMode += "x";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0004) == 0x0004){
			fileMode += "r";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0002) == 0x0002){
			fileMode += "w";
		}else {
			fileMode += "-";
		}
		
		if((code & 0x0001) == 0x0001){
			fileMode += "x";
		}else {
			fileMode += "-";
		}
		
		return fileMode;
	}
	
	/**
	 * returns user ID
	 * @return Short userID
	 */
	public short userID(){
		return vol.byteBuf.getShort(pointer+2);

	}
	
	/**
	 * Finds lower size of the Inode
	 * @return int lowerSize
	 */
	public int lowerSize(){
		return vol.byteBuf.getInt(pointer+4);
				
	}
	
	/**
	 * Last access time
	 * @return String lastAccess time
	 */
	public String lastAccess(){
		long temp = vol.byteBuf.getInt(pointer+8);
		DateFormat dateForm = new SimpleDateFormat("MMM dd hh:mm");
		Date d = new Date(temp*1000);
		String tempS = dateForm.format(d);
		
		return tempS;
	}
	
	/**
	 * Creation time
	 * @return String creationTime
	 */
	public String creationTime(){
		long temp = vol.byteBuf.getInt(pointer+12);
		DateFormat dateForm = new SimpleDateFormat("MMM dd hh:mm");
		Date d = new Date(temp*1000);
		String tempS = dateForm.format(d);
		
		return tempS;
	}
	
	/**
	 * Last modified Time
	 * @return String lastModified
	 */
	public String lastModified(){
		long temp = vol.byteBuf.getInt(pointer+16);
		DateFormat dateForm = new SimpleDateFormat("MMM dd hh:mm");
		Date d = new Date(temp*1000);
		String tempS = dateForm.format(d);
		
		return tempS;
	}
	
	/**
	 * Deleted Time
	 * @return String deletedTime
	 */
	public String deletedTime(){
		long temp = vol.byteBuf.getInt(pointer+20);
		DateFormat dateForm = new SimpleDateFormat("MMM dd hh:mm");
		Date d = new Date(temp*1000);
		String tempS = dateForm.format(d);		
		
		return tempS;
	}
	
	/**
	 * Gets Group ID
	 * @return short groupID
	 */
	public short groupID(){
		return vol.byteBuf.getShort(pointer+24);

	}
	
	/**
	 * Gets the number of links
	 * @return short numLinks
	 */
	public short numLinks(){
		return vol.byteBuf.getShort(pointer+26);
		
	}
	
	/**
	 * Gets 12 Direct pointers
	 * @return long[] pointers
	 */
	public long[] pointers(){
		long[] temp = new long[12];
		int temp1 = 0;
		int temp2 = pointer + 40;
		
		for(int count = 0; count <12; count++){
			temp[count] = vol.byteBuf.getInt(temp2+temp1);
			temp1 += 4;
		}
		
		
		return temp;
	}
	
	public int indirect(){
		return vol.byteBuf.getInt(pointer+88);

	}
	
	public int dIndirect(){
		return vol.byteBuf.getInt(pointer+92);

	}
	
	public int tIndirect(){
		return vol.byteBuf.getInt(pointer+96);

	}
	
	/**
	 * Gets the upper size
	 * @return int upperSize()
	 */
	public int upperSize(){
		return vol.byteBuf.getInt(pointer+108);
		
	}
	
	public void printInode(){

		System.out.println("------------------------------");
		System.out.println("File Mode:\t"+fileMode());
        System.out.println("User ID:\t"+userID());
        System.out.println("Lower Size:\t"+lowerSize());
        System.out.println("Accessed:\t"+lastAccess());
        System.out.println("Created:\t"+creationTime());
        System.out.println("Modified:\t"+lastModified());
        System.out.println("Deleted:\t"+deletedTime());
        System.out.println("Group ID:\t"+groupID());
        System.out.println("Hard Links:\t"+numLinks());
        long[] temp = pointers();
        
        for(int count = 0; count< temp.length; count++){
			System.out.println("Pointer "+(count+1)+":\t"+temp[count]);
		}
        
        System.out.println("Indirect:\t"+indirect());
        System.out.println("Double I:\t"+dIndirect());
        System.out.println("Triple I:\t"+tIndirect());
        System.out.println("Upper Size:\t"+upperSize());
	
	}
	
}
