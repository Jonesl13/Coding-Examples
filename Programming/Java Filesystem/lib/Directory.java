import java.util.*;

public class Directory{
	
	public Volume vol;
	public int start;
	public int size;
	public int count;
	public ArrayList <FileInfo> fileArray = new ArrayList<FileInfo>();
	private FileInfo[] infoArray;
	
	public Directory(Volume vol,int start, int size){
		
		this.vol = vol;
		this.start = start;
		this.size = size;
		count = start;
		
		readDir();
		
	}
	
	/**
	* Reads through the ext2 file form the given point for the amount of bytes.
	* Loops through each line, adding it to an ArrayList
	*/
	public void readDir(){
		
		int lineSize = 0,
			offset = 0, 
			inode;
		
		short length;
		
		byte nameLen, 
			 fileType;
			 
		String fileName = new String();
		
		while(count < start+size){
			
			inode = vol.byteBuf.getInt(count);
			count += 4;
			
			length = vol.byteBuf.getShort(count);
			count += 2; 

			nameLen = vol.byteBuf.get(count);
			count += 1;
			
			fileType = vol.byteBuf.get(count);
			count += 1;
			
			for(int count1 = count; count1 < count+nameLen; count1++){
			fileName = fileName + Character.toString((char)vol.byteBuf.get(count1));
			
			}

			count = count+ (int)length - 8;
			
			fileArray.add(new FileInfo(inode,length,nameLen,fileType,fileName));
			fileName = "";
		}
		
	}
		
	/**
	* Returns a FileInfo array after converting it from ArrayList 
	* @return FileInfo[] infoArray
	*/
	public FileInfo[] getFileInfo(){
		
		infoArray = new FileInfo[fileArray.size()];
		
		for(int count = 0; count < fileArray.size(); count++){
				infoArray[count] = fileArray.get(count);
		}
		return infoArray;
	}
}
