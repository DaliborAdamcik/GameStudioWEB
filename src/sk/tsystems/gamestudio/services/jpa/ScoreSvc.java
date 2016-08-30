package sk.tsystems.gamestudio.services.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.ScoreEntity;
import sk.tsystems.gamestudio.services.ScoreService;

public class ScoreSvc extends JpaConnector implements ScoreService {

	public ScoreSvc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addScore(ScoreEntity score) {
		EntityManager em = getEntityManager();
		beginTransaction();
		em.persist(score);
		commitTransaction();

		return true; // TODO when returns false?
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ScoreEntity> topScores(GameEntity game) {
		Query que = getEntityManager().createQuery("SELECT s FROM ScoreEntity s WHERE s.game = :game ORDER BY s.score DESC")
				.setParameter("game", game).setMaxResults(10);
		return que.getResultList();
	}

}
