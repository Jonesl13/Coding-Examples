import java.util.*;
public class startServer
   {
	private int bufferSize, num_users, num_servers, elements, divElements, divElements1;
  	Buffer b;													

    public startServer(int bufferSize, int num_users, int num_servers, int elements){
																						//Creates execution scenario between user and webservers on buffer
		this.bufferSize = bufferSize;
		this.num_users = num_users;
		this.num_servers = num_servers;
		this.elements = elements;
		
		long startTime = System.currentTimeMillis();		
																	
																						//Instantiate all objects (webserver, users, buffer)
		semaphore Sem = new semaphore(1);

		
		b = new Buffer(bufferSize,Sem);	
		
		user[] users = new user[num_users];
		webserver[] servers = new webserver[num_servers];
		Thread[] threads = new Thread[num_users+num_servers];
		int[] userEl = new int[num_users];												//Two integer arrays used to count each element total per user/server
		int[] servEl = new int[num_servers];
		int tempElements = elements;
		int counter = 0;
						
						
																						//Equally subdividing the total elements amongst the users and servers
		
		while (tempElements > 0){
		
			userEl[counter]++;
			tempElements--;
			
			if (counter < num_users-1){
				counter++;
				
			}else{
				counter = 0;
			}
			
		
		}
		
		
		
		tempElements = elements;
		counter = 0;
		
		while (tempElements > 0){
		
			servEl[counter] ++;
			tempElements--;
			
			if (counter < num_servers-1){
				counter++;
				
			}else{
				counter = 0;
			}
			
		
		}
		
		for (int count = 0; count < num_users; count++){								//Creating instances of the users class and adding them to threads
			
			users[count] = new user(count, userEl[count], b, bufferSize);
			threads[count] = new Thread(users[count]);
		}
		
		int count1 = num_users;
		
		for(int count = 0; count < num_servers; count++){								//Creating instances of the webserver class and adding them to threads
		
			servers[count] = new webserver(count, servEl[count], b, bufferSize);
			threads[count1] = new Thread(servers[count]);
			count1++;
			
		}
		
		
		
		for (int count = 0; count < num_users+num_servers; count++){					//Starting all the threads
   
			threads[count].start();
   
		}
		
		for (int count = 0; count < num_users+num_servers; count++){					//Joining the threads to ensure the program waits for all to finish before continuing
			try{
				threads[count].join();
	   
			}catch(InterruptedException r){
				System.out.println(r);
			}
		}

		System.out.println("-----------------------");
		
		for (int count = 0; count < num_users; count++){
			System.out.println("User "+count+" created a total of "+users[count].getElementTotal()+" elements");
		
		}	
		
		for (int count = 0; count < num_servers; count++){
			System.out.println("Consumer "+count+" consumed a total of "+servers[count].getElementTotal()+" elements");
		
		}	

		System.out.println("-----------------------");
		System.out.println("Buffer has " + b.getRemaining() + " elements remaining");				//Check to see buffer if all elements produced from users have been successfully removed by webservers
		System.out.println("-----------------------");	
		
		for (int count = 0; count < num_users; count++){
			System.out.println("User thread "+count+" is alive: "+threads[count].isAlive());
		}
		int threadNumber = 0;
		for (int count = num_users; count< num_users+num_servers; count ++){
			
			System.out.println("Server thread "+threadNumber+" is alive: "+threads[count].isAlive());
			threadNumber++;
		}
					
		long endTime = System.currentTimeMillis();
		System.out.println("-----------------------");
		System.out.println("Program took " + (endTime - startTime) + " milliseconds to complete");		
	
    }
  
	public static void main(String[] args){
	
		int bufferSize, num_users, num_servers, elements;
		  
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter buffer capacity");
		bufferSize = sc.nextInt();
		
		System.out.println("Enter number of users");
		num_users = sc.nextInt();
		
		System.out.println("Enter number of servers");
		num_servers = sc.nextInt();
		
		System.out.println("Enter total number of elements");
		elements = sc.nextInt();

		startServer start = new startServer(bufferSize, num_users, num_servers, elements);
	}
}
