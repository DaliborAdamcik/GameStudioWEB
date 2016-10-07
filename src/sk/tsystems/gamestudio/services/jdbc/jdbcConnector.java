package sk.tsystems.gamestudio.services.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;


public abstract class jdbcConnector implements AutoCloseable{
	// configuration for database connection  
	private String host = null;
	private String user = null;
	private String password = null;
	private int timeout = 10; // 10 seconds default timeout for verify delay of connection
	private boolean configurationLoaded = false;
	private Connection dbCon = null; // an connection to database  
	
	
	public jdbcConnector() {
		super();
		
		if(!configurationLoaded)
			loadConfiguration();
	}
	
	private void loadConfiguration()
	{
		Properties prop = new Properties();

		try(InputStream input = 
				this.getClass().getClassLoader().getResourceAsStream("META-INF/svcjdbc.conf") ) {
			prop.load(input);
			
			host = prop.getProperty("host");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			
			if(host==null || user==null || password==null)
				throw new NullPointerException("Missconfigured required property: host, user or password");


			String tmout = prop.getProperty("timeout"); // timeout is optional configuration flag
			if(tmout!= null)
			try
			{
				timeout = Integer.parseInt(tmout);
			}
			catch (NumberFormatException e) {}
			
			if(timeout<10)
				throw new NumberFormatException("We dont support timeout less than 10 seconds. Please, reconfigure.");
				
			configurationLoaded = true;
		} 
		catch (Exception e) {
			throw new RuntimeException("JDBC svc connector can't read conf file", e);
		}
	}
	
	private void establishConn()
	{
        if(dbCon!= null)
        	tryCloseDBConn();

        try
        {
            Class.forName("oracle.jdbc.OracleDriver");
			try {
				dbCon = DriverManager.getConnection(host, user, password);
			} catch (SQLException e) {
				// retry on exception to connect
				dbCon = DriverManager.getConnection(host, user, password);
			}
        }
        catch(Exception e)
        {
        	dbCon = null; 
        	throw new RuntimeException("JDBC svc: Can't connect to database server", e);
        }
	}
	
	private void tryCloseDBConn()
	{
    	try {
    		dbCon.close();
    	} catch (Exception e) { }
    	dbCon = null;
	}
	
	private boolean verifyConn()
	{
		try {
			return dbCon.isValid(timeout);
		} catch (NullPointerException | SQLException e) {
		}
		return false;
	}
	
	public Connection conn()
	{
		if( !verifyConn() )
			establishConn();
 
		return dbCon;
	}

	@Override
	public void close() /* throws Exception */{
		tryCloseDBConn();
	}
}	
	

