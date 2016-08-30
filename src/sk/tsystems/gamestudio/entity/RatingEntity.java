package sk.tsystems.gamestudio.entity;
import sk.tsystems.gamestudio.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import sk.tsystems.gamestudio.entity.GameEntity;

@Entity
@Table(name="JPA_RATING")
public class RatingEntity {
	@Id
	@GeneratedValue
	@Column(name = "RATID")
	private int id;
	@Column(name = "RATPOINTS")
	private int rating;
	@OneToOne
	@JoinColumn(name="GAMEID")
	private GameEntity game;
	@OneToOne
	@JoinColumn(name="USRID")
	private UserEntity user;
	
	public RatingEntity() { // constructor for JPA
		this(null, null, 0, 0);
	}
	public RatingEntity(GameEntity game, UserEntity user, int rating, int id) {
		super();
		this.game = game;
		this.user = user;
		this.rating = rating;
		this.id = id;
	}
	
	public RatingEntity(GameEntity game, UserEntity user, int rating) {
		this(game, user, rating, 0);
	}

	/*
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}*/

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return rating;
	}

	public GameEntity getGame() {
		return game;
	}

	public UserEntity getUser() {
		return user;
	}
	
	
	
}


