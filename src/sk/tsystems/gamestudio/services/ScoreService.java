package sk.tsystems.gamestudio.services;

import java.util.Map;
import java.util.List;

import sk.dalik.stats.DTO.TwoGameDTO;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.entity.UserEntity;

public interface ScoreService  extends AutoCloseable {
	boolean addScore(ScoreEntity score);
	List<ScoreEntity> topScores(GameEntity game);
	Map<String, List<ScoreEntity>> topScoresHourly(GameEntity game);
	Map<String, List<TwoGameDTO>> topScoresHourly2g();
	int gameRuns(UserEntity usr, GameEntity game);
}
