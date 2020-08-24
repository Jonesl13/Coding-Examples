import java.util.*;

public class Hash_Map{

	private int numPeople, numRepeats;
	private Map<String,Person> hMap = new HashMap<>();
	private long totalTime,startTime,endTime;
	
	public Hash_Map(int numPeople, int numRepeats){
		
		this.numPeople = numPeople;
		this.numRepeats = numRepeats;
		addPeople();
	}
	
	public void addPeople(){
		
		
		Person tempP;
		
		for(int count = 0; count < numRepeats; count++){
		
			hMap.clear();
			
			startTime = System.nanoTime();
			
			for(int count1 = 0; count1 < numPeople; count1++){
				
				tempP = new Person(Integer.toString(count1), count1);
				hMap.put(tempP.getName(),tempP);
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashMap took " + temp + " nanoseconds to add");	
	}
	
	
	public void getPerson(String name){
		
		for(int count = 0; count < numRepeats; count++){
		
			startTime = System.nanoTime();
			
			hMap.get(name);
			
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
			
			if (hMap.containsKey(name) == true){
			
				found = true;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashMap took " + temp + " nanoseconds to check for person "+name);
	
	}
	
	
	public void getNth(int n){
	
		Person tempP = new Person("",0);
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			tempP = hMap.get(n);
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashMap took " + temp + " nanoseconds to retrieve person at position "+n);
	
	}
	
	public void removePerson(String name){
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			hMap.remove(name);
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("HashMap took " + temp + " nanoseconds to remove person "+name);
	
	}

}


