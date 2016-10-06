package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.UserService;

public class UserSvc extends jdbcConnector implements UserService {
	private final String SELECT_QI = "SELECT USRID, UNAME, PWD FROM USRS WHERE USRID = ?";
	private final String SELECT_QN = "SELECT USRID, UNAME, PWD, email FROM USRS WHERE ? in (UNAME, email)";
	private final String INSERT_Q = "INSERT INTO USRS (UNAME, USRID, PWD, EMAIL) VALUES (?, USRID_SEQ.nextval, '1234', 'newusr'||USRID_SEQ.nextval||'@gamestudio' )";
	private final String UPDATE_Q = "UPDATE USRS SET UNAME = ?, PWD = ? WHERE USRID = ?";
	private static UserService instance = null;
	
	private UserEntity myAcc = null;
	
	public UserSvc() {
		super();
		instance = this;
	}
	
	static UserService getInstance()
	{
		if(instance == null)
			instance = new UserSvc();
		return instance;
	}

	
	private UserEntity getUserSql(String Stat, Object o)
	{
		try(PreparedStatement stmt = this.conn().prepareStatement(Stat))
        {
			if (o instanceof String)
				stmt.setString(1, (String) o);
			else
			if (o instanceof Integer)
				stmt.setInt(1, (Integer) o);
			else
				throw new RuntimeException("Invalid parameter class: "+o.getClass().getName());
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	if(rs.next())
	        	{	
	        		UserEntity usr = new UserEntity(rs.getInt(1), rs.getString(2)); 
	        		usr.setPassword(rs.getString(3));
	        		return usr;
	        	}
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public UserEntity getUser(int id) {
		return getUserSql(SELECT_QI, id);
	}

	@Override
	public UserEntity getUser(String name) {
		return getUserSql(SELECT_QN, name);
	}

	@Override
	public UserEntity me() {
		return myAcc;
	}

	@Override
	public boolean auth(String name, String password) {
		UserEntity usr = getUser(name);
		
		if(usr==null || usr.getPassword().compareTo(password)!=0) 
			return false;
		
		
		myAcc = usr;
		return true;
	}

	@Override
	public UserEntity addUser(String name) { // TODO temp function
        try(PreparedStatement stmt = this.conn().prepareStatement(INSERT_Q))
        {
	        stmt.setString(1, name);
	        if(stmt.executeUpdate()>0)
	        {
	        	UserEntity usr = getUser(name); 
	        	return usr;
	        }
        } catch (SQLException e) {
        	e.printStackTrace();
		}
        
        return null;
	}

	@Override
	public void setCurrUser(UserEntity user) {
		this.myAcc = user;
	}

	@Override
	public boolean updateUser(UserEntity user) {
        try(PreparedStatement stmt = this.conn().prepareStatement(UPDATE_Q))
        {
	        stmt.setString(1, user.getName());
	        stmt.setString(2, user.getPassword());
	        stmt.setInt(3, user.getID());
	        if(stmt.executeUpdate()>0)
	        	return true;
        } catch (SQLException e) {
        	e.printStackTrace();
		}
        
        return false;
	}

}
