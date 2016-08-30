package sk.tsystems.gamestudio.services.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.services.GameService;

public class GameSvc extends JpaConnector implements GameService {

	public GameSvc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public GameEntity getGame(int id) {
		try
		{
			EntityManager em = getEntityManager();
			Query que = em.createQuery("SELECT g FROM GameEntity g WHERE g.id = :id").setParameter("id", id);
			return (GameEntity) que.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameEntity> listGames() {
		EntityManager em = getEntityManager();
		Query que = em.createQuery("SELECT g FROM GameEntity g");
		return que.getResultList();
	}

	@Override
	public boolean addGame(GameEntity game) {
		// TODO check game is addeded (by cllass name)
		// TODO we need to set game.setID(0) there
		EntityManager em = getEntityManager();
		beginTransaction();
		em.persist(game);
		commitTransaction();
		return true; // TODO
	}

	@Override
	public GameEntity getGameByLet(String name) {
		try
		{
			EntityManager em = getEntityManager();
			Query que = em.createQuery("SELECT g FROM GameEntity g WHERE g.servletpath = :svp").setParameter("svp", name);
			return (GameEntity) que.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

}
