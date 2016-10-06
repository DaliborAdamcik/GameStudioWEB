package sk.tsystems.gamestudio.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import sk.tsystems.gamestudio.entity.UserEntity;
import sk.tsystems.gamestudio.entity.GameEntity;

@Entity
@Table(name="JPA_SCORE")
public class ScoreEntity  {
	
	@Id
	@GeneratedValue
	@Column(name = "SCOID")
	int ident; // not used in class, only for JPA 
	
	@Column(name = "SCORE")
	private int score;
	
	@Column(name = "DESC")
	private int desc;
	
	@Column(name = "SCDAT")
	private Date date;
	@OneToOne
	@JoinColumn(name="GAMEID")
	private GameEntity game;
	@OneToOne
	@JoinColumn(name="USRID")
	private UserEntity user;
	
	public ScoreEntity() // constructor for JPA
	{
		this(null, null, 0, 0, null);
	}
	
	public ScoreEntity(GameEntity game, UserEntity user, int score, int desc, Date datum) {
		super();
		this.game = game;
		this.user = user;
		this.score = score;
		this.date = datum;
		this.desc = desc;
	}
	
	public ScoreEntity(GameEntity game, UserEntity user, int score) {
		this(game, user, score, 0, new Date());
	}

	public ScoreEntity(GameEntity game, UserEntity user, int score, int desc) {
		this(game, user, score, desc, new Date());
	}
	
	public ScoreEntity(int score, int desc) {
		this(null, null, score, desc, new Date());
	}
	
	public Date getDate() {
		return date;
	}

	public int getScore() {
		return score;
	}

	public GameEntity getGame() {
		return game;
	}

	public UserEntity getUser() {
		return user;
	}
	
	public int getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return "ScoreEntity [user=" + user + ", date=" + date + ", score=" + score + "]";
	}

	public void setGame(GameEntity game) {
		this.game = game;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
}