package sk.tsystems.gamestudio.services;

import java.util.Map;
import java.util.List;

import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;

public interface ScoreService  extends AutoCloseable {
	boolean addScore(ScoreEntity score);
	List<ScoreEntity> topScores(GameEntity game);
	Map<String, List<ScoreEntity>> topScoresHourly();
}
