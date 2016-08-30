package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.ScoreService;
import sk.tsystems.gamestudio.services.UserService;

public class ScoreSvc extends jdbcConnector implements ScoreService {
	private final String INSERT_Q = "INSERT INTO SCORE (USRID, GAMEID, DAT, SCORE) VALUES (?, ?, ?, ?)";
	private final String SELECT_Q = "SELECT * FROM (SELECT USRID, DAT, SCORE FROM SCORE WHERE GAMEID = ? ORDER BY SCORE.SCORE DESC) WHERE ROWNUM <= 10  ";
	private UserService user;

	public ScoreSvc() {
		super();
		this.user = UserSvc.getInstance();
	}

	@Override
	public boolean addScore(ScoreEntity score) {
        try(PreparedStatement stmt = this.conn().prepareStatement(INSERT_Q))
        {
	        stmt.setInt(1, user.me().getID()); // prevent add comment as another user
	        stmt.setInt(2, score.getGame().getID());
	        
	        stmt.setDate(3, new java.sql.Date(score.getDate().getTime()));
	        stmt.setInt(4, score.getScore());

	        stmt.executeUpdate();
        } catch (SQLException e) {
/*        	if(e instanceof java.sql.SQLIntegrityConstraintViolationException)
        	throw new DuplicationException("Person "+person.getName()+" ("+person.getPhoneNumber()+") already exists.");*/	
			e.printStackTrace();
		}
        
        return true;
	}

	@Override
	public List<ScoreEntity> topScores(GameEntity game) {
		List<ScoreEntity> results = new ArrayList<>();
		
		try(PreparedStatement stmt = this.conn().prepareStatement(SELECT_Q))
        {
			stmt.setInt(1, game.getID());
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	while(rs.next())
	        	{
	        		UserEntity usr = user.getUser(rs.getInt(1));
	        		if(usr==null)
	        			throw new RuntimeException("DB integrity problem: user #"+rs.getInt(1)+" not found.");

	        		ScoreEntity sco = new ScoreEntity(game, usr, rs.getInt(3), rs.getDate(2));
	        		results.add(sco);
	        	}
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	
}
