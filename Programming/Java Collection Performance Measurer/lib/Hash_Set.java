import java.util.*;

public class Hash_Set{

	private int numPeople, numRepeats;
	private HashSet<Person> hSet = new HashSet<Person>();
	private long totalTime,startTime,endTime;
	
	public Hash_Set(int numPeople, int numRepeats){
		
		this.numPeople = numPeople;
		this.numRepeats = numRepeats;
		addPeople();
	}
	
	public void addPeople(){
		
		
		
		for(int count = 0; count < numRepeats; count++){
		
			hSet.clear();
			
			startTime = System.nanoTime();
			
			for(int count1 = 0; count1 < numPeople; count1++){
				
				hSet.add(new Person(Integer.toString(count1), count1));
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashSet took " + temp + " nanoseconds to add");	
	}
	
	public void getPerson(String name){
		
		Person tempP = new Person(name,0);
		
	
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			for(Person P:hSet){
				
				if (P.equals(tempP)){
					break;
				}
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashSet took " + temp + " nanoseconds to retrieve person "+name);
	}
	
	public void check(String name){
	
		boolean found = false;

		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			if (hSet.contains(name) == true){
			
				found = true;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashSet took " + temp + " nanoseconds to check for person "+name);
	
	}
	
	public void getNth(int n){
	
		Person tempP = new Person("",0);
		int counter = 0;
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			for(Person P:hSet){
				
				if (counter == n){
					break;
				}
				counter++;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashSet took " + temp + " nanoseconds to retrieve person at position "+n);
	
	}
	
	
	public void removePerson(String name){
		
		Person tempP = new Person(name,0);	
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			for(Person P:hSet){
				
				if (P.equals(tempP)){
					hSet.remove(P);
					break;
				}
				count++;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
			
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashSet took " + temp + " nanoseconds to remove person "+name);
	}

}


