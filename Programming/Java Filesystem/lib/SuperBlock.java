import java.nio.*;
import java.io.*;

public class SuperBlock{
	
	public Volume vol;
	
	public SuperBlock(Volume vol){
		this.vol = vol;
	}
	
	/**
	* Gets the magic numbers
	* @return int vol.byteBuf.getInt(1024+56)
	*/
	public int getMagic(){
		return vol.byteBuf.getInt(1024+56);
	}
	
	/**
	* Gets the Total number of inodes
	* @return int vol.byteBuf.getInt(1024);
	*/
	public int totalInodes(){
		return vol.byteBuf.getInt(1024);
	}
	
	/**
	* Gets the Total number of blocks
	* @return int vol.byteBuf.getInt(1024+4);
	*/
	public int totalBlocks(){
		return vol.byteBuf.getInt(1024+4);
	}
	
	/**
	* Gets the Total number of blocks per group
	* @return int vol.byteBuf.getInt(1024+32);
	*/
	public int blocksPerGroup(){
		return vol.byteBuf.getInt(1024+32);
	}
	
	/**
	* Gets the Total number of inodes per group
	* @return int vol.byteBuf.getInt(1024+40);
	*/
	public int inodesPerGroup(){
		return vol.byteBuf.getInt(1024+40);
	}
	
	/**
	* Gets the Total inode size
	* @return int vol.byteBuf.getInt(1024+88);
	*/
	public int inodeSize(){
		return vol.byteBuf.getInt(1024+88);
	}
	
	/**
	* Gets the volume label in String
	* @return String temp
	*/
	public String volumeLabel(){
		String temp = new String();
		
		for(int count = 1024+120; count < 1024+120+16; count++){
			temp = temp + Character.toString((char)vol.byteBuf.get(count));
			
		}
		return temp;
	}
	
	public void printSuperBlock(){
		
		System.out.println("------------------------------");
		System.out.println("Magic Number:\t"+getMagic());
        System.out.println("Total Inodes:\t"+totalInodes());
        System.out.println("Total Blocks:\t"+totalBlocks());
        System.out.println("Blocks P/G:\t"+blocksPerGroup());
        System.out.println("Inodes P/G:\t"+inodesPerGroup());
        System.out.println("Inode Size:\t"+inodeSize());
        System.out.println("Volume Label:\t"+volumeLabel());
	
	}

}
