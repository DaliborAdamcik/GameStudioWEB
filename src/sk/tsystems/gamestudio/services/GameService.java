package sk.tsystems.gamestudio.services;

import java.util.List;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.SettingEntity;

public interface GameService extends AutoCloseable {
	GameEntity getGame(int id);
	GameEntity getGameByLet(String name);
	List<GameEntity> listGames();	
	boolean addGame(GameEntity game);
	<T> T gameSetting(GameEntity game, String name, Class <T> clazz, T defaVal);
	void saveSetting(GameEntity game, String name, Object value);
	List<SettingEntity> listSettings();
}
