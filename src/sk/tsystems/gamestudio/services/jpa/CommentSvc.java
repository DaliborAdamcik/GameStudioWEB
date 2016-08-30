package sk.tsystems.gamestudio.services.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import sk.tsystems.gamestudio.entity.CommentEntity;
import sk.tsystems.gamestudio.entity.GameEntity;
import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.CommentService;

public class CommentSvc extends JpaConnector implements CommentService  {
	int limitcomments = 10;
	public CommentSvc() {
	}

	@Override
	public boolean addComment(CommentEntity comment) {
		comment.setID(0); // on add we set zero ID, JPA sets new one
		EntityManager em = getEntityManager();
		beginTransaction();
		em.persist(comment);
		commitTransaction();

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommentEntity> commentsFor(GameEntity game) {
		try
		{
			EntityManager em = getEntityManager();
			//beginTransaction();

			Query que = em.createQuery("SELECT c FROM CommentEntity c WHERE c.game = :game").setParameter("game", game).setMaxResults(limitcomments);
		
			return que.getResultList();
		}
		catch (NoResultException e) {
			return new ArrayList<CommentEntity>();
		}
		/*finally
		{
			commitTransaction();
		}*/
	}

	@Override
	public List<CommentEntity> commentsFor(UserEntity user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLimit(int limit) {
		if (limit>0)
		limitcomments = limit; 
	}
}
