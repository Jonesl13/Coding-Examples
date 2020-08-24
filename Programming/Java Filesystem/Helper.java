public class Helper{
	
	/**
	* Prints hex and ascii code for a given byte array 
	* @param byte[]bytes the byte array to read from
	*/
	public void dumpHexBytes(byte[]bytes)throws Exception{
	
		System.out.println("------------------------------");
	
		for (int count = 0; count < bytes.length; count++){
				
				//If you have reached the 8th character in the line
				if (count % 8 == 0 && count > 0){
						
					System.out.print("| ");
					
				}
				//If you have reached the 16th character in the line
				if (count % 16 == 0 && count > 0){
					
					count -= 16;
					printAscii(count, bytes);
					count += 16;
					
					System.out.println();
				
			}	//Prints out the read byte array as hex
				System.out.print((String.format("%02X", bytes[count]))+" ");
		}
		
		System.out.print("| ");		
		printAscii(bytes.length-16,bytes);
		System.out.println();
	}
	
	
	
	/**
	* Called to print the previously read line as ascii
	* @param byte[] bytes the byte array to read from
	* @param int pointer the starting point to read from
	*/
	public void printAscii(int pointer, byte[] bytes){
		
		int temp;
		
		for(int count = pointer; count < pointer+16; count++){
			
			if (count == pointer + 8 && count >1){
						
					System.out.print(" | ");
					
			}
			 
			temp = Integer.parseInt(String.format("%02X", bytes[count]), 16);
			
			//To make sure only valid ascii characters are printed
			if((temp > 31)&&(temp < 127)){
			
				System.out.print((char)temp); 
			}else{
			
				System.out.print(".");
			}
		}
	}
	

}
