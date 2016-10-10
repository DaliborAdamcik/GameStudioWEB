package sk.tsystems.gamestudio.entity;

public class SettingEntity {
	private GameEntity game;
	private String name;
	private String value;
	/**
	 * @param game
	 * @param name
	 * @param value
	 */
	public SettingEntity(GameEntity game, String name, String value) {
		super();
		this.game = game;
		this.name = name;
		this.value = value;
	}
	public GameEntity getGame() {
		return game;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
}
