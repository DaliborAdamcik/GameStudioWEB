package sk.dalik.stats.DTO;

import sk.tsystems.gamestudio.entity.UserEntity;

public class TwoGameDTO {
	private UserEntity usr;
	private long score;
	private String date;
	private String hour;
	
	/**
	 * @param usr
	 * @param score
	 * @param date
	 * @param hour
	 */
	public TwoGameDTO(UserEntity usr, long score, String date, String hour) {
		super();
		this.usr = usr;
		this.score = score;
		this.date = date;
		this.hour = hour;
	}
	public UserEntity getUser() {
		return usr;
	}
	public long getScore() {
		return score;
	}
	public String getDate() {
		return date;
	}
	public String getHour() {
		return hour;
	}
	public String getScoreTime() {
		long millis = this.score % 1000;
		long secs = this.score / 1000;
		long mins = secs / 60;
		secs = secs % 60;
		return String.format("%d:%d.%d", mins, secs, millis);
	}
	
}
