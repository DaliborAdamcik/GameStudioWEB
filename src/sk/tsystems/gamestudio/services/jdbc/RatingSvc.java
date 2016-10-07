package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.RatingEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.RatingService;
import sk.tsystems.gamestudio.services.UserService;

abstract class RatingSvc extends CommentSvc implements RatingService {
	private UserService users;
	private final String UPDATE_Q = "UPDATE RATING SET RATING = ?, DAT = current_timestamp WHERE usrid = ? AND gameid = ?"; 
	private final String INSERT_Q = "INSERT INTO rating (usrid, gameid, rating) values (?, ?, ?)"; 

	private final String SELECT_MY_Q = "SELECT RATING FROM RATING WHERE GAMEID = ? AND USRID = ?";
	private final String SELECT_GA_Q = "SELECT (SELECT SUM(rating) FROM RATING WHERE GAMEID = ?), (SELECT COUNT(*) FROM RATING) FROM DUAL";

		
	public RatingSvc() {
		super();
		this.users = this;
	}

	@Override
	public void addRating(RatingEntity rating) {
		if(updateRating(rating))
				return;

		try(PreparedStatement stmt = this.conn().prepareStatement(INSERT_Q))
        {
	        stmt.setInt(1, users.me().getID()); // prevent addiding as another user
	        stmt.setInt(2, rating.getGame().getID());
	        stmt.setInt(3, rating.getRating());
	        stmt.executeUpdate();
        } catch (SQLException e) {
/*        	if(e instanceof java.sql.SQLIntegrityConstraintViolationException)
        	throw new DuplicationException("Person "+person.getName()+" ("+person.getPhoneNumber()+") already exists.");*/	
			e.printStackTrace();
		}
	}

	public boolean updateRating(RatingEntity rating) {
        try(PreparedStatement stmt = this.conn().prepareStatement(UPDATE_Q))
        {
	        stmt.setInt(1, rating.getRating());
	        stmt.setInt(2, users.me().getID()); // prevent addiding as another user
	        stmt.setInt(3, rating.getGame().getID());

	        return stmt.executeUpdate()>0;
        } catch (SQLException e) {
			e.printStackTrace();
		}
        
        return false;
		
	}
	
	@Override
	public RatingEntity myRating(GameEntity game, UserEntity user) {
		try(PreparedStatement stmt = this.conn().prepareStatement(SELECT_MY_Q))
        {
			stmt.setInt(1, game.getID());
			stmt.setInt(2, user.getID());
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	if(rs.next())
        			return new RatingEntity(game, user, rs.getInt(1));
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public double gameRating(GameEntity game) {
		try(PreparedStatement stmt = this.conn().prepareStatement(SELECT_GA_Q))
        {
			stmt.setInt(1, game.getID());
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	if(rs.next() && rs.getInt(2)>0)
        			return rs.getDouble(1) / rs.getInt(2);
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
