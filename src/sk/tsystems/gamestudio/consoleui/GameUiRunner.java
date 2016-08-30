package sk.tsystems.gamestudio.consoleui;

public interface GameUiRunner {
	public enum RunTarget {UI_CONSOLE, UI_WINDOW, UI_WEB};
	public enum ReturnState {RS_EXIT, RS_SUCCES, RS_FAIL};

	public ReturnState runCsl(RunTarget target);
	public int getScore();
}
