package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sk.dalik.stats.DTO.TwoGameDTO;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.ScoreService;
import sk.tsystems.gamestudio.services.UserService;

abstract class ScoreSvc extends RatingSvc implements ScoreService {
	private final String INSERT_Q = "INSERT INTO SCORE (USRID, GAMEID, DAT, SCORE, descript) VALUES (?, ?, ?, ?, ?)";
	private final String SELECT_Q = "SELECT * FROM (SELECT USRID, DAT, SCORE, descript, datFmt FROM scoretable WHERE GAMEID = ? AND scoretable.dat>= (select last_hr from lasthr) ) WHERE ROWNUM <= 10";
	private final String SELECT_HOURLY = "select * from scoretablehourly";
	private final String SELECT_HOURLY2G = "select * from scoretablehourly2g";
	private UserService user;

	public ScoreSvc() {
		super();
		this.user = this;
	}

	@Override
	public boolean addScore(ScoreEntity score) {
        try(PreparedStatement stmt = this.conn().prepareStatement(INSERT_Q))
        {
	        stmt.setInt(1, user.me().getID()); // prevent add comment as another user
	        stmt.setInt(2, score.getGame().getID());
	        
	        stmt.setDate(3, new java.sql.Date(score.getDate().getTime()));
	        stmt.setInt(4, score.getScore());
	        stmt.setInt(5, score.getDesc());

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

	        		ScoreEntity sco = new ScoreEntity(game, usr, rs.getInt(3), rs.getInt(4), rs.getDate(2));
	        		results.add(sco);
	        	}
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@Override
	public Map<String, List<ScoreEntity>> topScoresHourly() {
		TreeMap<String, List<ScoreEntity>> map = new TreeMap<>();
				
		
		try(PreparedStatement stmt = this.conn().prepareStatement(SELECT_HOURLY))
        {
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	while(rs.next())
	        	{
	        		String hourly = rs.getString(8);
	        		List<ScoreEntity> forhour = map.get(hourly);
	        		if(forhour==null) {
	        			forhour = new ArrayList<>();
	        			map.put(hourly, forhour);
	        		}
	        		// usrid, gmaeid, dat, 
	        		UserEntity usr = user.getUser(rs.getInt(1));
	        		GameEntity gam = getGame(rs.getInt(2));
	        		
	        		ScoreEntity sco = new ScoreEntity(gam, usr, 
	        				rs.getInt(4), rs.getInt(5), 
	        				rs.getDate(3));
	        		forhour.add(sco);
	        	}
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map.descendingMap();
	}

	public int gameRuns(UserEntity usr, GameEntity game) {
		try(PreparedStatement stmt = this.conn().prepareStatement("select count(*) as gamecount from score where usrid = ? and gameid = ?"))
        {
			stmt.setInt(1, usr.getID());
			stmt.setInt(2, game.getID());
			
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	if(rs.next())
	        		return rs.getInt(1);
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;		
	}
	
	@Override
	public Map<String, List<TwoGameDTO>> topScoresHourly2g() {
		TreeMap<String, List<TwoGameDTO>> map = new TreeMap<>();
				
		
		try(PreparedStatement stmt = this.conn().prepareStatement(SELECT_HOURLY2G))
        {
        	try(ResultSet rs = stmt.executeQuery())
        	{
	        	while(rs.next())
	        	{
	        		String hourly = rs.getString(6);
	        		List<TwoGameDTO> forhour = map.get(hourly);
	        		if(forhour==null) {
	        			forhour = new ArrayList<>();
	        			map.put(hourly, forhour);
	        		}
	        		// usrid, gmaeid, dat, 
	        		UserEntity usr = user.getUser(rs.getInt(1));
	        		
	        		TwoGameDTO sco = new TwoGameDTO(usr, 
	        				rs.getLong(2), rs.getString(4), 
	        				rs.getString(6));
	        		forhour.add(sco);
	        	}
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map.descendingMap();
	}
	
	
}
