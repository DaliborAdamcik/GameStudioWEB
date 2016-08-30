package sk.tsystems.gamestudio.game.stones.ui;

import sk.tsystems.gamestudio.game.stones.core.Field;

public class StonesUI extends sk.tsystems.gamestudio.consoleui.ConsoleInput implements sk.tsystems.gamestudio.consoleui.GameUiRunner {
	private Field field;

	public StonesUI() {
		//field = Field.load(); // we dont need load at all
		//if (field == null) {
			field = new Field(4, 4);
		//}
	}

	@Override
	public ReturnState runCsl(RunTarget target) {
		if (target.equals(RunTarget.UI_CONSOLE))
		{
			StonesUIcsl game = new StonesUIcsl(field);
			if(game.runCsl()) // false for exit
			{
				if(field.isSolved())
					return ReturnState.RS_SUCCES;
				else
					return ReturnState.RS_FAIL;
			}
//			else
//				field.save();// dont need
		}
		return ReturnState.RS_EXIT;
	}

	@Override
	public int getScore() {
        int score = field.getRowCount()*field.getColumnCount();
        score = field.getPlayingSeconds()*10 / score;

		return score;
	}
}
