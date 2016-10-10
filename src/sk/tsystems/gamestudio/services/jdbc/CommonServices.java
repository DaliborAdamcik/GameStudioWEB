package sk.tsystems.gamestudio.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sk.tsystems.gamestudio.entity.GameEntity;

public class CommonServices extends ScoreSvc {
	public CommonServices() {
		super();
	}

	@Override
	public <T> T gameSetting(GameEntity game, String name, Class<T> clazz, T defaVal) {
		try (PreparedStatement stmt = this.conn()
				.prepareStatement("select val from gamsets where gameid = ? and sname = ? ")) {
			stmt.setInt(1, game.getID());
			stmt.setString(2, name);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getObject(1, clazz);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return defaVal;
	}

	@Override
	public void saveSetting(GameEntity game, String name, Object value) {
		try (PreparedStatement stmt = this.conn()
				.prepareStatement("insert into gamsets (gameid, sname, val) values (?, ?, ?)")) {
			stmt.setInt(1, game.getID());
			stmt.setString(2, name);
			stmt.setObject(3, value);
			stmt.executeUpdate();/*
									 * try(ResultSet rs = stm.executeQuery()) {
									 * if(rs.next()) { return rs.getObject(1,
									 * clazz); }
									 * 
									 * }
									 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// return null;
	}

}
