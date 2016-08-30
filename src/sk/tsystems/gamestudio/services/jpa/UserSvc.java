package sk.tsystems.gamestudio.services.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.services.UserService;

public class UserSvc extends JpaConnector implements UserService {
	private UserEntity current = null;

	public UserSvc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean auth(String name, String password) { // TODO temporary auth  
		try 
		{
			UserEntity usr = getUser(name);
			
			if(usr.getPassword().compareTo(password)==0)
			{
				setCurrUser(usr);
				return true;
			}
		}
		catch (NullPointerException e)
		{
		}
		return false;
	}

	@Override
	public UserEntity getUser(int id) {
		try
		{
			Query que = getEntityManager().createQuery("SELECT u FROM UserEntity u WHERE u.id = :id").setParameter("id", id);
			return (UserEntity) que.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public UserEntity getUser(String name) {
		try
		{
			Query que = getEntityManager().createQuery("SELECT u FROM UserEntity u WHERE u.name = :name").setParameter("name", name);
			return (UserEntity) que.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public UserEntity me() {
		return current;
	}

	@Override
	public UserEntity addUser(String name) { // TODO Temporary function
		UserEntity usr = getUser(name);
		if(usr!=null)
			return usr;

		usr = new UserEntity(0,name);
		EntityManager em = getEntityManager();
		beginTransaction();
		em.persist(usr);
		commitTransaction();
		return usr;
	}

	@Override
	public void setCurrUser(UserEntity user) {
		this.current = user;
	}

	@Override
	public boolean updateUser(UserEntity user) {
		try
		{
			EntityManager em = getEntityManager();
			beginTransaction();
			em.persist(user);
			commitTransaction();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

}
