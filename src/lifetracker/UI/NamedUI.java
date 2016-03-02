package lifetracker.UI;

import java.util.Scanner;

import lifetracker.logic.Logic;

public class NamedUI implements UI {

	private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, "
			+ "Spend less time planning "
			+ "so you always know what's next.";
	
	static Scanner scanner = new Scanner(System.in);
	
	public NamedUI(Logic l){
		welcomeMessage();
	}
	
	private static void welcomeMessage() {
		System.out.printf(MESSAGE_WELCOME);
	}
}
