package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsDTO {
	private class DBconnector extends sk.tsystems.gamestudio.services.jdbc.jdbcConnector{};
	private int userCount;
	private int gameCount;
	private int scoreCount;
	private int ratingCount;
	private double avgRatingGS;
	private List<GSStatPlayer> mostactiveplayers;
	private List<GSStatGame> gamestat;
	
	public StatisticsDTO() throws Exception {
		mostactiveplayers = new ArrayList<>();
		gamestat = new ArrayList<>();
		try(DBconnector conn = new DBconnector())
		{
			readCoutsGS(conn); // read global statistics (counts) 
			readMostActivePlayer(conn); // read players most playng game
			readGameStat(conn);
		} 
	}
	
	private void readCoutsGS(DBconnector conn) throws SQLException
	{
		try(PreparedStatement stmt = conn.conn().prepareStatement("select usercount, gamecount, scorecount, ratingcount, ratingsum from gscounts"))
		{
			try(ResultSet rs = stmt.executeQuery())
			{
				if(rs.next())
				{
					userCount = rs.getInt(1); 
					gameCount = rs.getInt(2);
					scoreCount = rs.getInt(3);
					ratingCount = rs.getInt(4);
					avgRatingGS= rs.getDouble(5) / rs.getInt(4);
				}
			}
		}
	}
	
	
	private void readGameStat(DBconnector conn) throws SQLException
	{																	// 1 		2		3		4	5		6		7		8	
		try(PreparedStatement stmt = conn.conn().prepareStatement("select gameid, gname, ratsum, ratc, scoc, scomax, scomin, comc  from gsgamestat"))
		{
			try(ResultSet rs = stmt.executeQuery())
			{
				while(rs.next())
					gamestat.add(new GSStatGame(rs.getInt(1), rs.getString(2), rs.getLong(3), 
							rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), ratingCount));
			}
		}
	}

	private void readMostActivePlayer(DBconnector conn) throws SQLException
	{
		try(PreparedStatement stmt = conn.conn().prepareStatement("select usrid, uname, cnt from GSUSRMAXSCORE"))
		{
			try(ResultSet rs = stmt.executeQuery())
			{
				while(rs.next())
					mostactiveplayers.add(new GSStatPlayer(rs.getInt(1), rs.getInt(3), rs.getString(2)));
			}
		}
	}
	
	public class GSStatPlayer {
		private int uid;
		private int plays;
		private String name;
		GSStatPlayer(int uid, int plays, String name) {
			this.uid = uid;
			this.plays = plays;
			this.name = name;
		}
		public int getUid() { return uid; }
		public int getPlays() {	return plays;}
		public String getName() {return name;}
		@Override
		public String toString() {
			return "GSStatPlayer [uid=" + uid + ", plays=" + plays + ", name=" + name + "]";
		}
	}

	public class GSStatGame {
		private int gid;
		private String name;
		private int ratingcount;
		private int runcount;
		
		private int scoremax;
		private int scoremin;
		private int commentCnt;

		private double ratinggame; // calculated
		private double ratingGS; // calculated

		GSStatGame(int gameid, String gname, long ratsum, int ratc, int scoc, int scomax, int scomin, int comc, int gsRatingCount)
		{
			super();
			this.gid = gameid;
			this.name = gname;
			this.ratingcount = ratc;
			this.runcount = scoc;
			this.scoremax = scomax;
			this.scoremin = scomin;
			this.commentCnt = comc;

			// calculated values
			this.ratinggame = ((double)ratsum / ratc);
			this.ratingGS = ((double)ratsum / gsRatingCount);
		}

		public int getGid() {return gid; }
		public String getName() { return name; }
		public int getRatingCount() { return ratingcount; }
		public int getRunCount() {return runcount;}
		public int getScoreMax() {return scoremax;}
		public int getScoreMin() {return scoremin;}
		public int getCommentCnt() {return commentCnt;}
		public double getRatingGame() {	return ratinggame;}
		public double getRatingGS() {return ratingGS;}

		@Override
		public String toString() {
			return "GSStatGame [gid=" + gid + ", name=" + name + ", ratingcount=" + ratingcount + ", runcount="
					+ runcount + ", scoremax=" + scoremax + ", scoremin=" + scoremin + ", commentCnt=" + commentCnt
					+ ", ratinggame=" + ratinggame + ", ratingGS=" + ratingGS + "]";
		}
	}

	// TODO: on changes please remove toString and all getters. 
	// after modifications generate new ones
	
	@Override
	public String toString() {
		return "StatisticsDTO [userCount=" + userCount + ", gameCount=" + gameCount + ", scoreCount=" + scoreCount
				+ ", ratingCount=" + ratingCount + ", avgRatingGS=" + avgRatingGS + ", mostactiveplayers="
				+ mostactiveplayers + ", gamestat=" + gamestat + "]";
	}

	public int getUserCount() {
		return userCount;
	}

	public int getGameCount() {
		return gameCount;
	}

	public int getScoreCount() {
		return scoreCount;
	}

	public int getRatingCount() {
		return ratingCount;
	}

	public double getAvgRatingGS() {
		return avgRatingGS;
	}

	public List<GSStatPlayer> getMostactiveplayers() {
		return mostactiveplayers;
	}

	public List<GSStatGame> getGamestat() {
		return gamestat;
	}
}
