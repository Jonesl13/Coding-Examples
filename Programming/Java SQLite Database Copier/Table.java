import java.util.ArrayList;
public class Table{
	
	private String tableName = new String();
	private String primaryKey = "";
	private String fKey = new String();
	private ArrayList<String> columns = new ArrayList<>();
	private int colCount = 0;
	private boolean hasPK = false;

	public Table(String tableName){
		
		this.tableName = tableName;
	}
	
	public void addColumn(String colName, String colType){
		
		columns.add(colName+" "+colType);
		colCount++;
	}
	
	public void addPrimaryKey(String pKey){
		
		if(hasPK == true){
			for(int count = 0; count < colCount; count++){
				String[] parts1 = columns.get(count).split(" ");

				if(parts1[0].equals(pKey)){
					columns.set(count-1, parts1[0]+" "+parts1[1]);
					columns.add("primary key("+pKeyList(pKey)+")");
				}
			}
		
		}else if(hasPK == false){		
				
			for(int count = 0; count < colCount; count++){
				String[] parts2 = columns.get(count).split(" ");
				if(parts2[0].equals(pKey)){
					columns.set(count, parts2[0]+" "+parts2[1]+" primary key");
					pKeyList(pKey);
				}
			}
			
			hasPK = true;
		}
		
	}
	
	
	private String pKeyList(String pKey){
		if(primaryKey.equals("")){
			primaryKey = pKey;
		}else{
			primaryKey = primaryKey + ", " + pKey;
		}
		return primaryKey;
	}
	
	public void addForeignKey(String fKey){
		columns.add(fKey);

	}
	
	
	
	public String[] getColumns(){
		ArrayList<String> cols = new ArrayList<>();
		
		for(int count = 0; count < colCount; count++){
			
			String[] parts = columns.get(count).split(" ");
			
			cols.add(parts[0]);
			
		}
		String[] temp = cols.toArray(new String[cols.size()]);
		return temp;
	}
	
	public String[] getTypes(){
		ArrayList<String> cols = new ArrayList<>();
		
		for(int count = 0; count < colCount; count++){
			
			String[] parts = columns.get(count).split(" ");
			
			cols.add(parts[1]);
			
		}
		String[] temp = cols.toArray(new String[cols.size()]);
		return temp;
	}
	
	
	public String getName(){
		
		return tableName;
	
	}
	
	public void printAll(){
		
		System.out.print("CREATE TABLE "+tableName+" (");
		
		
		for(int count = 0; count < columns.size(); count++){
			System.out.print(columns.get(count));
			if(count < columns.size()-1){
				System.out.print(", ");
			}
		}
							
		System.out.println(");");
	}

}
