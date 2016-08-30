package sk.tsystems.gamestudio.services;

import java.util.List;

import sk.tsystems.gamestudio.entity.GameEntity;

public interface GameService extends AutoCloseable {
	GameEntity getGame(int id);
	GameEntity getGameByLet(String name);
	List<GameEntity> listGames();	
	boolean addGame(GameEntity game);
}
