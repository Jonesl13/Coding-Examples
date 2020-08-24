import java.util.*;
public class user implements Runnable{		
							
  private int id;
  private int n = 0;
  private int num_elements;
  private int total; 
  private int sum = 0;
  private int element = 0;
  public static Buffer buf;

		  
  public user(int i, int el, Buffer b, int total){	        			//User arguments: User ID, number of elements to add, and buffer
		  id = i; num_elements = el; buf = b; this.total = total;
		  element = el;
		  }
  
  public  void add_elements()
   { 
	
		while (num_elements > 0)
		{						

			   int temp = buf.add(n,id,num_elements);					//Passing the num_elements to the buf.add method
			   
			   if(num_elements != temp){								//If num_elements has changed (was able to add correctly) do n++
					num_elements = temp;
					n++;
			   
			   }

		}
	 		
   }
   
  public int getElementTotal(){
   
	return element;
   }

   
   public void run(){
	 
	   add_elements();
		
	}

  } 
