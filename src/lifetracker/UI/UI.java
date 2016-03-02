package lifetracker.UI;

import lifetracker.logic.Logic;

import java.util.Scanner;

public class UI {

	private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, "
			+ "Spend less time planning "
			+ "so you always know what's next.";
	private static final String MESSAGE_INPUT = "Command: ";
	
	static Scanner scanner = new Scanner(System.in);
	
	public UI(Logic l){
		welcomeMessage();
		executeUntilExit(l);
	}
	
	public void welcomeMessage() {
		System.out.printf(MESSAGE_WELCOME);
	}
	
	public void executeUntilExit(Logic l) {
		while(true) {
			System.out.printf(MESSAGE_INPUT);
			String input = scanner.nextLine();
			for (String resultLine: l.executeCommand(input).getResultLines()) {
				System.out.println(resultLine);
			}
		}
	}
}
