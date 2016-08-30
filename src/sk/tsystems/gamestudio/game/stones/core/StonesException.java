package sk.tsystems.gamestudio.game.stones.core;

@SuppressWarnings("serial")
public class StonesException extends RuntimeException {

	public StonesException(String message, Throwable cause) {
		super(message, cause);
	}

	public StonesException(String message) {
		super(message);
	}	
}
