import java.util.*;

public class Buffer							//Provides data and operations onto the fixed-length buffer
  {     									
	private LinkedList<Object> buf_list;				
	private semaphore Sem;
	private int count = 0, buf_size, sum = 0;
	private boolean isFull = false, isEmpty = true;
	
	public Buffer(int buf_size, semaphore Sem){						//Buffer creation, with n indicating the maximum capacity
	
		buf_list = new LinkedList<Object>();
		
		this.buf_size = buf_size;
		this.Sem = Sem;

		
    }
        
     public int add(int n, int id, int num_elements){
		 
		 Sem.P();
		 
		if (buf_list.size()<buf_size){														//If buffer is not full
			
		buf_list.add(n);																	//Add n to buffer
		System.out.println("User "+id+" added an element "+buf_list.size()+"/"+buf_size);	//Printing the add message
		num_elements--;																		
		count++;
			 
		}else{																				//Since num_elements is not decreasing here, the while loop in user will run again
		
			 System.out.println("Buffer full - User now sleeping");
			 
			
		}
		Sem.V();
		return num_elements;
		
		
		
			
	 }
	 
	 public int remove(int n, int id, int num_elements){
		 
		Sem.P();
		
		
		if(buf_list.size()>0){																	//If buffer not empty
			
			buf_list.pop();
			System.out.println("Serv "+id+" removed an element "+buf_list.size()+"/"+buf_size);
			num_elements--;
			count--;
			
		}else{
			System.out.println("Buffer empty - Server now waiting");
		}
		
		Sem.V();
		return num_elements;
		 
	 }
	 
	 public int getRemaining(){
	 
		return count;
	 }
	 
	
}	  
