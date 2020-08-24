import java.util.*;

public class semaphore{
	
    private int count = 0;
	
	public semaphore(int init_val)
	{
		count = init_val;
	}
	
	public semaphore(){
	}
	
	public synchronized void P()
	{
		count = count - 1;
		if (count < 0) try{
			wait();
		}catch(Exception e){System.out.println(e);} 
	}
	
	public synchronized void V()
	{
		count = count + 1;

		notify();
	}


}

