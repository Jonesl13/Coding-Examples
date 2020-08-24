public class Main {

	private void go()
	{
		//Change the string entered here to reference other db
		DbBasic1 db = new DbBasic1("Chinook.db");
		
		db.close();
	};

	public static void main(String [ ] args)
	{
		Main myMain = new Main();
		myMain.go();
	}

} 
