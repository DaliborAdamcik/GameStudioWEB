package sk.tsystems.gamestudio.services.jdbc.creator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sk.tsystems.gamestudio.services.jdbc.jdbcConnector;

public class CreatorOracle extends jdbcConnector {
	private int numque = 0;
	private int succ = 0;
	private int ignore = 0;
	
	public CreatorOracle() {
		// TODO Auto-generated constructor stub
	}
	
	public void run()
	{
		try
		{
			conn().setAutoCommit(false);
			dowork();
			conn().commit();
			System.out.println("*** commit");
		}
		catch (SQLException e) {
			System.err.println("Error during executing SQL: "+e.getMessage());
			//e.printStackTrace();

			try {
				System.err.println("*** rollback");
				conn().rollback();
			} catch (SQLException e1) {
				System.out.println("** Rollback failed:");
				e1.printStackTrace();
			}
		}
		System.out.printf("------- SUMARRY -------\n\tQueries: %d\n\tSuccess: %d\n\tIgnored: %s\n", numque, succ, ignore);
	}
	
	private void dowork() throws SQLException
	{
		Pattern strictPattern = Pattern.compile("#\\$(\\s)*strict(\\s)*(on|off)(\\s)*");
		Matcher matcher;
		
		String[] statements = loadStatements();
		boolean strictmode = true;
		if(statements==null)
		{
			System.out.println("*** nothing to execute ...***\n\t... exiting");
			return;
		}
		
		for(String statement: statements)
		{
			StringBuilder querybuild = new StringBuilder();
			
			for(String line: statement.split("\n"))
			{
				line = line.trim();
				if(line.length()==0) // empty line
					continue;
					
				matcher = strictPattern.matcher(line.toLowerCase()); 
				if(matcher.matches()) // is internal directive
				{
					strictmode = "on".equals(matcher.group(3));
					System.out.println("** strict mode: "+strictmode);
				}
				else
				querybuild.append(line.trim()).append(" ");
					
			}
			
			String query = querybuild.toString();
			
			if(query.length()>0)
				executeQuerry(query, strictmode);
		}
	}
	
	private String[] loadStatements()
	{
		try
		(InputStream input = 
			this.getClass().getClassLoader().getResourceAsStream("META-INF/create.sql");
		InputStreamReader read = new InputStreamReader(input);	
		BufferedReader buffer = new BufferedReader(read);
		){
			String s = null;
			StringBuilder sb = new StringBuilder();
			do {
				s = buffer.readLine();
				if(s!=null)
					sb.append(s).append("\n");
			}
			while(s!=null);
			
			String[] statements = sb.toString().replaceAll("(\\/\\*).*(\\*\\/)", "").split(";"); // split by ; remove comments
			return statements;
		}
		catch (Exception e) {
			System.err.println("Cant read file 'META-INF/create.sql':");
			e.printStackTrace();
			return null;
		}
	}
	
	private void executeQuerry(String query, boolean strictmode) throws SQLException
	{
		numque++;
		System.out.printf("Executing query: \n\t%s\n", query);		
		try(PreparedStatement exc = conn().prepareStatement(query))
		{
			exc.executeQuery(); // TODO check result set
			System.out.println("\t* OK");
			succ++;
		}
		catch(SQLException e)
		{
			if(strictmode)
			{
				System.err.println("\t! FAIL: "+e.getMessage());
				throw e;
			}
			else
			{
				System.err.println("\t! IGNORE:  "+e.getMessage());
				ignore++;
			}
		}
	}
}
