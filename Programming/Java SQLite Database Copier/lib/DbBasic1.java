import java.sql.*;
import java.io.File;
import java.util.ArrayList;

public class DbBasic1 {
	/**
	 * Set to true to enable debug messages
	 */
	boolean	debug	= false;

	/**
	 * Name of database driver
	 *
	 * @see Class#forName( )
	 */
	private static final String JDBC_DRIVER		= "org.sqlite.JDBC";

	/**
	 * URI prefix for database location
	 *
	 * @see java.sql.DriverManager#getConnection( )
	 */
	private static final String DATABASE_LOCATION	= "jdbc:sqlite:";
	
	/**
	 * {@link java.sql.Connection} for the database
	 *
	 * @see #getConnection( )
	 */
	protected Connection	con	= null;

	/**
	 * Filesystem path to database
	 *
	 * @see #DbBasic(String)
	 */
	public    String	dbName	= null;
	
	/**
	 * Outputs a stacktrace for debugging and exits
	 * <p>
	 * To be called following an {@link java.lang.Exception}
	 *
	 * @param message	informational String to display
	 * @param e		the Exception
	 */
	public void notify( String message, Exception e ) {
		System.out.println( message + " : " + e );
		e.printStackTrace ( );
		System.exit( 0 );
	}
	
	/**
	 * Establish JDBC connection with database
	 * <p>
	 * Autocommit is turned off delaying updates
	 * until commit( ) is called
	 */
	 
	private ArrayList<Table> tables = new ArrayList<>();
	private String tableName = new String();
	private String fTableName = new String();
	private String fColName = new String();
	private String colName = new String();
	private String fKey = new String();
	 
	private void readMetaData(){
		try {	
			
			DatabaseMetaData meta = con.getMetaData();
			
			//Extracting the table names
			ResultSet tableNames = meta.getTables(null, null, null, new String[]{"TABLE"});

			while(tableNames.next())
			{
				
				tableName = tableNames.getString("TABLE_NAME");
				
				//Creating a temporary table instance
				Table temp = new Table(tableName);
				
				//Extracting the column names
				ResultSet columns = meta.getColumns(null,null, tableNames.getString("TABLE_NAME"), null);
				ResultSet primaryKey = meta.getPrimaryKeys(null,null, tableNames.getString("TABLE_NAME"));
				ResultSet foreignKey = meta.getImportedKeys(null, null, tableNames.getString("TABLE_NAME"));
				
				while(columns.next())
				{
					String columnName = columns.getString("COLUMN_NAME");
					String dataType = columns.getString("TYPE_NAME");
					
					temp.addColumn(columnName,dataType);

				}
				while(primaryKey.next()){
					temp.addPrimaryKey(primaryKey.getString("COLUMN_NAME"));

				}
				while(foreignKey.next()){
					fTableName =foreignKey.getString("PKTABLE_NAME");
					fColName =foreignKey.getString("PKCOLUMN_NAME");
					colName =foreignKey.getString("FKCOLUMN_NAME");
					fKey = "foreign key("+colName+") references "+fTableName+"("+fColName+")";
					
					temp.addForeignKey(fKey);
					
				}
				
				//Adding the table instance to the ArrayList
				tables.add(temp);
				
				
			}
			
				
		} catch (SQLException e ) {
			System.out.println(e);
				
		}	
		
	}
	
	private void printData(){
		
		try{
			
			for(int count = 0; count < tables.size(); count++){
				
				String query = "SELECT * FROM "+tables.get(count).getName();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				
				tables.get(count).printAll();
				
				String[] temp = tables.get(count).getColumns();	
				String[] type = tables.get(count).getTypes();	
				
				while(rs.next()){
					System.out.print("INSERT INTO "+tables.get(count).getName()+" VALUES(");
					for(int i = 0; i< temp.length; i++){

						if(type[i].equals("INT")||type[i].equals("LONG")||type[i].equals("FLOAT")){
							System.out.print(rs.getString(temp[i]));
						}else{
							System.out.print("'"+rs.getString(temp[i])+"'");
						}
						
						if(i < temp.length-1){
							System.out.print(", ");
						}
					}
					System.out.println(");");
				}
				
			}
			
			
		} catch (SQLException e ) {
			System.out.println(e);
				
		}	
	
	}
	 
	private void getConnection() {

		try {
			con = DriverManager.getConnection(
					  DATABASE_LOCATION
					+ dbName);

			/*
			 * Turn off AutoCommit:
			 * delay updates until commit( ) called
			 */
			con.setAutoCommit(false);
			
		}
		catch ( SQLException sqle ) {
			notify( "Db.getConnection database location ["
					+ DATABASE_LOCATION
					+ "] db name["
					+ dbName
					+ "]", sqle);
			close( );
		}
	}
	
	/**
	 * Opens database
	 * <p>
	 * Confirms database file exists and if so,
	 * loads JDBC driver and establishes JDBC connection to database
	 */
	private void open() {
		File dbf = new File( dbName );

		if ( dbf.exists( ) == false ) {
			System.out.println(
				 "SQLite database file ["
				+ dbName
				+ "] does not exist");
			System.exit( 0 );
		}
	
		try {
			Class.forName( JDBC_DRIVER );
			getConnection( );
		}
		catch ( ClassNotFoundException cnfe ) {
			notify( "Db.Open", cnfe );
		}

		if ( debug )
			System.out.println( "Db.Open : leaving" );
	}
	
	/**
	 * Close database
	 * <p>
	 * Commits any remaining updates to database and
	 * closes connection
	 */
	public final void close() {
		try {
			con.commit( ); // Commit any updates
			con.close ( );
		}
		catch ( Exception e ) {
			notify( "Db.close", e );
		};
	}

	/**
	 * Constructor
	 * <p>
	 * Records a copy of the database name and
	 * opens the database for use
	 *
	 * @param _dbName	String holding the name of the database,
	 * 			for example, C:/directory/subdir/mydb.db
	 */
	public DbBasic1( String _dbName ) {
		dbName = _dbName;

		if ( debug )
			System.out.println(
				  "Db.constructor ["
				+ dbName
				+ "]");

		open();
		readMetaData();
		printData();
	}
}
