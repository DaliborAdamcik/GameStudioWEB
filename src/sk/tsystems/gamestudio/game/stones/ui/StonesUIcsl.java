package sk.tsystems.gamestudio.game.stones.ui;

import sk.tsystems.gamestudio.game.stones.core.Field;

public class StonesUIcsl extends sk.tsystems.gamestudio.consoleui.ConsoleInput {
	private Field field;
	private boolean running = true;

	public StonesUIcsl(Field field) {
		super();
		this.field = field;
	}

	public void show() {
		System.out.printf("Time: %03d s\n", field.getPlayingSeconds());
		for (int row = 0; row < field.getRowCount(); row++) {
			for (int column = 0; column < field.getColumnCount(); column++) {
				int value = field.getValueAt(row, column);
				if (value == Field.EMPTY_CELL) {
					System.out.printf("  ");
				} else {
					System.out.printf("%2d", value);
				}
				System.out.print("  ");
			}
			System.out.println();
		}
	}

	private void processInput() {
		System.out.print("Enter input: ");
		String line = readLine().toLowerCase().trim();

		try{
			int value = Integer.parseInt(line);
			if(field.move(value))
				return;
			
		}catch(NumberFormatException e) {}
		
		switch (line) {
		case "w":
		case "up":
			field.moveUp();
			break;
		case "a":
		case "left":
			field.moveLeft();
			break;
		case "s":
		case "down":
			field.moveDown();
			break;
		case "d":
		case "right":
			field.moveRight();
			break;
		case "x":
		case "exit":
			running = false;
		default:
			System.out.println("What's wrong?");
			break;
		}
	}

	public boolean runCsl() {
		do {
			show();
			processInput();
		} while (!field.isSolved() && running);

		if(!running)
			return false;
		
		if(field.isSolved())
			System.out.println("You won the Game!");
		else
			System.out.println("You lost the Game!");
		
		return true;
	}
}
