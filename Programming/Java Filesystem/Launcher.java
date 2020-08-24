import java.nio.*;
import java.io.*;
import java.util.*;

public class Launcher{
	
	private Volume vol;
	private Helper help = new Helper();
    private Ext2File file;
	private SuperBlock sB;
	private GroupDesc[] gD;
	private Inode[] I;
	private Scanner sc;
	private int totalGD;
	private int inodesPG;
	private int count1;	
	private String dir = new String();
	private String userIn = new String();
	private byte[] buf;
	
	
	public void start()throws Exception{
		
		vol = new Volume("ext2fs");
		file = new Ext2File (vol);
		sB = new SuperBlock(vol);
		sc = new Scanner(System.in);
		dir = "~/root/";
		
		totalGD = sB.totalInodes()/sB.inodesPerGroup();
		inodesPG = sB.inodesPerGroup();
		count1 = 0;	
	
		buf = file.read(1024);
		
		gD = new GroupDesc[totalGD];
		I = new Inode[sB.totalInodes()];
		
		//Making multiple instances of the GroupDesc class depending on the superblock spec
		for(int count = 0; count < totalGD; count++){
			gD[count] = new GroupDesc(vol,count);

		}
		//Calculates the starting point for the inode
        int start = gD[(int)(2/sB.inodesPerGroup())].iTablePointer() * 1024+((2%sB.inodesPerGroup())-1)*sB.inodeSize();
		
		Inode inode = new Inode(vol,sB,start,2);
		cLI(inode);
	}
	
	/**
	 * Creates the command line interface that allows the user to traverse the ext2 system.
	 * Prints out different interfaces depending on the current directory.
	 * @param inode an instance of the Inode class
	 */
	public void cLI(Inode inode)throws Exception{

		Directory d = createDir(inode);
        FileInfo[] fileInfo = d.getFileInfo();
		
		while(true){
			
			System.out.print(dir);
			userIn = sc.next();
			
			//Lists out all the possible commands the user could input
			if(userIn.equalsIgnoreCase("Help")){
				
				System.out.println("------------------------------");
				System.out.println("ls:\tLists all the files in the current directory");
				System.out.println("info:\tLists the information of the current directory");
				System.out.println("dump:\tDump hex bytes of the super block");
				System.out.println("super:\tPrint out the super block information");
				System.out.println();

			}
			
			//Lists out the current contents of the directory
			if(userIn.equalsIgnoreCase("ls")){

				System.out.println("------------------------------");		
				for(int count = 0; count < fileInfo.length; count++){
					
					//Calculating the starting point for the next Inode
					int start = gD[(int)(fileInfo[count].getInode()/sB.inodesPerGroup())].iTablePointer() * 1024+((fileInfo[count].getInode()%sB.inodesPerGroup())-1)*sB.inodeSize();
						
					//Creating a new inode I
					Inode I = new Inode(vol,sB,start,fileInfo[count].getInode());
					
					System.out.println(I.fileMode()+"\t"+I.numLinks()+"\t"+I.userID()+"\t"+I.groupID()+"\t"+I.lowerSize()+"\t"+I.lastModified()+"\t"+fileInfo[count].getFileName());
					
				}
				System.out.println();
			}
			
			//Shows the info of the current directory's Inode
			if(userIn.equalsIgnoreCase("info")){
				inode.printInode();
			}
			
			//Prints the contents of the super block
			if(userIn.equalsIgnoreCase("dump")){
				help.dumpHexBytes(buf);
			}
			
			//Prints the information of the super block
			if(userIn.equalsIgnoreCase("super")){
				sB.printSuperBlock();
			}
			
			//Traverse the given fileInfo array
			for(int count = 0; count < fileInfo.length; count++){
			
				if(userIn.equals(fileInfo[count].getFileName())){
					
					//Calculating the starting point for the next Inode
					int start = gD[(int)(fileInfo[count].getInode()/sB.inodesPerGroup())].iTablePointer() * 1024+((fileInfo[count].getInode()%sB.inodesPerGroup())-1)*sB.inodeSize();
						
					//Creating a new inode I
					Inode I = new Inode(vol,sB,start,fileInfo[count].getInode());
					
					//Cheking if the next Inode points to a file
					if(I.fileMode().substring(0,1).equals("-")){
						System.out.print("Read start: ");
							int startByte = sc.nextInt();
						System.out.print("Read end: ");
							int endByte = sc.nextInt();
						readFile(I, startByte, endByte);
						
					}else{
						
						//If the user wants to go back a layer in the current directory
						if((userIn.equals(".."))&&(dir.length() > 7)){
							
							//Splitting the directory string into array to remove last word
							String[] dir1 = dir.split("/");
							dir = "";
							
							//concatinating the String array without the last added word
							for(int count1 = 0; count1 < dir1.length-1; count1++){
								dir += dir1[count1]+"/";
							}
							
						}else{
							
							dir += fileInfo[count].getFileName()+"/";
						}
												
						cLI(I);
					}
				}
			}
		}    
	}
	
	/**
	 * Reads the contents of the given Inode as a file instead of a directory.
	 * @param inode an instance of the Inode class
	 */
	public void readFile(Inode I, int s, int end){
		
		long[] temp = I.pointers();
		boolean found = false;
		int count = 0;
		
		//Finding the first available pointer
		while ((found == false)&&(count < 11)){
			
			if(temp[count] > 0){
				found = true;
			}else{
				count++;
			}
		}
		
		int start = 1024*(int)temp[count];
		int size =  end;
		int count1 = start+s;
		String fileText = new String();
		
		while(count1 < start+size){
			
			fileText += Character.toString((char)vol.byteBuf.get(count1));
			
			count1++;
		}
		System.out.println(fileText);
	}
	
	/**
	 * Creates an instance of the directory class given an inode.
	 * Uses first found pointer from given Inode
	 * @param Inode I an instance of the Inode class
	 * @return d a new directory instance
	 */
	public Directory createDir(Inode I){
		
		long[] temp = I.pointers();
		boolean found = false;
		int count = 0;
		
		while ((found == false)&&(count < 11)){
			
			if(temp[count] > 0){
				found = true;
			}else{
				count++;
			}
		}
		
		Directory d = new Directory(vol,1024*(int)temp[count], I.lowerSize());
		
		return d;
	}
	
}
