import java.util.*;

public class Linked_List{

	private int numPeople, numRepeats;
	private LinkedList<Person> lList = new LinkedList<Person>();
	private long totalTime,startTime,endTime;
	
	public Linked_List(int numPeople, int numRepeats){
		
		this.numPeople = numPeople;
		this.numRepeats = numRepeats;
		addPeople();
	}
	
	public void addPeople(){
		
		
		
		for(int count = 0; count < numRepeats; count++){
		
			lList.clear();
			
			startTime = System.nanoTime();
			
			for(int count1 = 0; count1 < numPeople; count1++){
				
				lList.add(new Person(Integer.toString(count1), count1));
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("LinkedList took " + temp + " nanoseconds to add");	
	}
	
	public void getPerson(String name){
		
		Person tempP = new Person(name,0);
		
	
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			for(Person P:lList){
				
				if (P.equals(tempP)){
				
					break;
				}
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("LinkedList took " + temp + " nanoseconds to retrieve person "+name);
	}
	
	public void check(String name){
	
		boolean found = false;

		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			if (lList.contains(name) == true){
			
				found = true;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("LinkedList took " + temp + " nanoseconds to check for person "+name);
	
	}
	
	public void getNth(int n){
	
		Person tempP = new Person("",0);
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			tempP = lList.get(n);
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("LinkedList took " + temp + " nanoseconds to retrieve person at position "+n);
	
	}
	
	
	public void removePerson(String name){
		
		Person tempP = new Person(name,0);
		
		for(int count = 0; count < numRepeats; count++){
		
			
			startTime = System.nanoTime();
			
			for(Person P:lList){
				
				if (P.equals(tempP)){
					lList.remove(P);
					break;
				}
				count++;
			}
			
			endTime = System.nanoTime();
			totalTime = totalTime + (endTime - startTime);
			
		}
		
		
		long temp = (totalTime/numRepeats);
		
		System.out.println("LinkedList took " + temp + " nanoseconds to remove person "+name);
	}
	
}

