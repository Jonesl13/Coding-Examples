public class FileInfo{
	
	private int inode;
	private short length;
	private byte nameLen;
	private byte fileType;
	private String fileName;
	
	public FileInfo(int inode, short length, byte nameLen, byte fileType, String fileName){
	
		this.inode = inode;
		this.length = length;
		this.nameLen = nameLen; 
		this.fileType = fileType;
		this.fileName = fileName;
	}
	
	/**
	 * Returns the inode number of the current file
	 * @return int inode
	 */
	public int getInode(){
		return inode;
	}
	
	/**
	 * Returns the length of the file in short 
	 * @return short length
	 */
	public short getLength(){
		return length;
	}
	
	/**
	 * Returns the name length of the file in byte
	 * @return byte nameLen
	 */
	public byte getNameLen(){
		return nameLen;
	}
	
	/**
	 * Returns the file type in bytes
	 * @return byte fileType
	 */
	public byte getFileType(){
		return fileType;
	}
	
	/**
	 * Returns the name of the current file in String
	 * @return String fileName
	 */
	public String getFileName(){
		return fileName;
	}
}
