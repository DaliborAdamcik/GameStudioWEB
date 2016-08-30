package sk.tsystems.gamestudio.game.minesweeper;

import sk.tsystems.gamestudio.consoleui.GameUiRunner;
import sk.tsystems.gamestudio.game.minesweeper.consoleui.ConsoleUI;
import sk.tsystems.gamestudio.game.minesweeper.core.Field;

/**
 * Main application class.
 */
public class Minesweeper implements GameUiRunner {
    /** User interface. */
    private UserInterface userInterface;
    private long startMillis;
    private static Minesweeper instance;
	private Settings setting;
	private Field field;
 
    /**
     * Constructor.
     */
	public Minesweeper() {
        instance = this;
        setting = Settings.load();
        this.field = new Field(setting.getRowCount(), setting.getColumnCount(), setting.getMineCount());
    }
	
	
	@Override
	public ReturnState runCsl(RunTarget target) {
		switch (target) {
			case UI_CONSOLE: // TODO default for this moment
			default:
				userInterface = new ConsoleUI();
				break;
		}
        startMillis = System.currentTimeMillis();
        userInterface.newGameStarted(field);

        switch(field.getState())
        {
			case FAILED:
				return ReturnState.RS_FAIL;
			case SOLVED:
				return ReturnState.RS_SUCCES;
			default:
				return ReturnState.RS_EXIT;
        }
	}

	@Override
	public int getScore() { // calculate score here
        int score = field.getRowCount()*field.getColumnCount();
        score = Minesweeper.getInstance().getPlayingSeconds()*10 / score;

		return score;
	}
	
	

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        Minesweeper min = new Minesweeper();
        min.runCsl(RunTarget.UI_CONSOLE);
    }
    
    public int getPlayingSeconds()
    {
    	long time =(System.currentTimeMillis() - startMillis); 
    	return (int)(time / 1000);
    }
    
    public static Minesweeper getInstance()
    {
    	return instance;
    }
    
	public Settings getSetting() {
		return setting;
	}

	public void setSetting(Settings setting) {
		this.setting = setting;
		this.setting.save();
	}
}
