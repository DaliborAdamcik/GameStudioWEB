package sk.tsystems.gamestudio.game.guessnumber.ui;

import sk.tsystems.gamestudio.consoleui.ConsoleInput;
import sk.tsystems.gamestudio.consoleui.GameUiRunner;
import sk.tsystems.gamestudio.game.guessnumber.core.GuessNumber;

public class GuesNumRun extends ConsoleInput implements GameUiRunner {
	private int score = 0;
	private long started;

	public GuesNumRun() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ReturnState runCsl(RunTarget target) {
		if(target.equals(RunTarget.UI_CONSOLE))
		{
			consolerun();
			return ReturnState.RS_SUCCES;
		}

		return ReturnState.RS_EXIT;
	}

	@Override
	public int getScore() {
		return score;
	}
	
	private void consolerun()
	{
		System.out.println("------> GuessTHEnumber <--------");
		int bounds = readInt("Bounds", 10, 10000);
		GuessNumber gue = new GuessNumber(bounds);
		started = System.currentTimeMillis();
		
		int trynum;

		do
		{
			trynum = readInt("number", 1, gue.getBound());
			trynum = gue.tryHit(trynum);
			
			if(trynum<0)
				System.out.println("* I guess lower number");
			
			if(trynum>0)
				System.out.println("* I guess higher number");
		}
		while(trynum!=0);
		System.out.println("*** You WON ***");

		score = (int) (System.currentTimeMillis() - started);
	}
	
	

}
