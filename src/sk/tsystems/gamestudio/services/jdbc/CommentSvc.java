package sk.tsystems.gamestudio.services.jdbc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sk.tsystems.gamestudio.entity.CommentEntity;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.CommentService;
import sk.tsystems.gamestudio.services.GameService;
import sk.tsystems.gamestudio.services.UserService;

abstract class CommentSvc extends UserSvc implements CommentService {
	private final String INSERT_Q = "INSERT INTO comments (usrid, gameid, dat, msg, COMID) VALUES (?, ?, ?, ?, COMID_SEQ.nextval)";
	private final String SELECT_Q = "SELECT COMID, USRID, GAMEID, DAT, MSG FROM comments WHERE %s = ? AND ROWNUM <= ? ORDER BY COMID DESC";
	private UserService user;
	private GameService game;
	private int limit = 6;
	
	public CommentSvc() {
		super();
		this.user = this;
		this.game = this;
	}

	@Override
	public boolean addComment(CommentEntity comment) {
		if(comment.getID()>0)
			return false; // we dont need to save already saved comment
		
        try(PreparedStatement stmt = this.conn().prepareStatement(INSERT_Q, new String[] {"COMID"}))
        {
	        stmt.setInt(1, user.me().getID()); // prevent add comment as another user
	        stmt.setInt(2, comment.getGameID());
	        
	        stmt.setDate(3, new java.sql.Date(comment.getDate().getTime()));
	        stmt.setString(4, comment.getComment());

	        stmt.executeUpdate();
	        
	        try(ResultSet rs = stmt.getGeneratedKeys())
	        {
		        if (rs.next() ) {
		            comment.setID(rs.getInt(1));
		        }
	        }
	        
        } catch (SQLException e) {
/*        	if(e instanceof java.sql.SQLIntegrityConstraintViolationException)
        	throw new DuplicationException("Person "+person.getName()+" ("+person.getPhoneNumber()+") already exists.");*/	
			e.printStackTrace();
		}
        
        return true;
	}

	private List<CommentEntity> doSelect(String sql, int who)
	{
		List<CommentEntity> results = new ArrayList<>();
		
		try(PreparedStatement stmt = this.conn().prepareStatement(sql))
        {
			stmt.setInt(1, who);
			stmt.setInt(2, limit);
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	while(rs.next())
	        	{
	        		UserEntity usr = user.getUser(rs.getInt(2));
	        		if(usr==null)
	        			throw new RuntimeException("DB integrity problem: user #"+rs.getInt(2)+" not found.");

	        		GameEntity gam = game.getGame(rs.getInt(3));
	        		if(gam==null)
	        			throw new RuntimeException("game not found.");

	        		results.add(new CommentEntity(gam, usr, rs.getString(5), rs.getInt(1), rs.getDate(4)));
	        	}
	        	
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	

	@Override
	public List<CommentEntity> commentsFor(GameEntity game) {
		return doSelect(String.format(SELECT_Q, "GAMEID"), game.getID());
	}

	@Override
	public List<CommentEntity> commentsFor(UserEntity user) {
		return doSelect(String.format(SELECT_Q, "USRID"), user.getID());
	}

	@Override
	public void setLimit(int limit) {
		if (limit>1)
			this.limit = limit;
	}

	
}
