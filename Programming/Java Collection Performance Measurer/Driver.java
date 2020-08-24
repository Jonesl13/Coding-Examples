import java.util.*;

public class Driver{
	
	

	public static void main(String[]args){
		
		int numPeople, numRepeats, testNumber;
		Random r = new Random();
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please enter the amount of people: ");
		numPeople = sc.nextInt();
		
		System.out.println("Please enter the amount of repeats: ");
		numRepeats = sc.nextInt();
		
		Array_List aList = new Array_List(numPeople, numRepeats);
		
		Linked_List lList = new Linked_List(numPeople, numRepeats);
		
		Hash_Set hashS = new Hash_Set(numPeople, numRepeats);
		
		Hash_Map hashM = new Hash_Map(numPeople, numRepeats);
		
		
		System.out.println("-----------------------");
		
		testNumber = r.nextInt(numPeople);
		
		
		aList.getPerson(Integer.toString(testNumber));
		
		lList.getPerson(Integer.toString(testNumber));
		
		hashS.getPerson(Integer.toString(testNumber));
		
		hashM.getPerson(Integer.toString(testNumber));
		
		
		System.out.println("-----------------------");
		
		
		aList.check(Integer.toString(testNumber));
		
		lList.check(Integer.toString(testNumber));
		
		hashS.check(Integer.toString(testNumber));
		
		hashM.check(Integer.toString(testNumber));
		
		
		System.out.println("-----------------------");
		
		
		aList.getNth(testNumber);
		
		lList.getNth(testNumber);
		
		hashS.getNth(testNumber);
		
		hashM.getNth(testNumber);
		
		
		System.out.println("-----------------------");
		
		
		aList.removePerson(Integer.toString(testNumber));
		
		lList.removePerson(Integer.toString(testNumber));
		
		hashS.removePerson(Integer.toString(testNumber));
		
		hashM.removePerson(Integer.toString(testNumber));
	}

}
