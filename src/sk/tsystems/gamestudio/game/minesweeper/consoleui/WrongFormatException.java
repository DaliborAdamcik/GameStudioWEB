package sk.tsystems.gamestudio.game.minesweeper.consoleui;

/**
 * Wrong format exception.
 */
class WrongFormatException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor.
     * @param message message
     */
    public WrongFormatException(String message) {
        super(message);
    }
}
