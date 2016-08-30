package sk.tsystems.gamestudio.game.guessnumber.core;

import java.util.Random;

public class GuessNumber {
	private int number;
	private int bound;

	public GuessNumber(int bound) {
		// TODO range check 0+
		this.bound = bound;
		Random ra = new Random();
		number = ra.nextInt(bound)+1;
	}
	
	public int tryHit(int hitNum)
	{
		return number - hitNum;
	}

	public int getBound() {
		return bound;
	}
}
