package lifetracker.UI;

import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

import java.util.Scanner;

public class UI {

    private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, Spend less time planning so you always know what's next.";
    private static final String MESSAGE_INPUT = "Command: ";

    static Scanner scanner = new Scanner(System.in);

    public UI(Logic l) {
        assert l != null;

        welcomeMessage();
        executeUntilExit(l);
    }

    public void welcomeMessage() {
        System.out.println(MESSAGE_WELCOME);
    }

    public void executeUntilExit(Logic l) {
        assert l != null;

        while (true) {
            System.out.printf(MESSAGE_INPUT);
            String input = scanner.nextLine();

            ExecuteResult result = l.executeCommand(input);

            System.out.println(result.getComment());
            System.out.println();

            for (String resultLine : result.getResultLines()) {
                System.out.println(resultLine);
            }
        }
    }
}
