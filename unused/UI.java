package lifetracker.UI;

import dnl.utils.text.table.TextTable;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

import java.util.List;
import java.util.Scanner;

//@@author A0091173J-unused
//Replaced by GUI
public class UI {

    private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, Spend less time planning so you always know what's next.";
    private static final String MESSAGE_INPUT = "Command: ";

    private static final String[] EVENT_HEADERS = {"ID", "Name", "Active?", "Start", "End", "Every"};
    private static final String[] TASK_HEADERS = {"ID", "Name", "Active?","Due", "Every"};

    private static final String EVENT_TITLE = "Events: ";
    private static final String TASK_TITLE = "Tasks: ";

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

        ExecuteResult result;

        do {
            System.out.printf(MESSAGE_INPUT);
            String input = scanner.nextLine();

            result = l.executeCommand(input);

            if (result.getType() == ExecuteResult.CommandType.DISPLAY) {

                printTable(EVENT_TITLE, EVENT_HEADERS, result.getEventList());
                printTable(TASK_TITLE, TASK_HEADERS, result.getTaskList());

            }

            if(result.getType() != ExecuteResult.CommandType.EXIT) {
                System.out.println(result.getComment());
                System.out.println();
            }

        } while (result.getType() != ExecuteResult.CommandType.EXIT);
    }

    private void printTable(String title ,String[] headers ,List<List<String>> data) {

        TextTable displayTable = new TextTable(headers, listToArray(data));

        System.out.println(title);
        displayTable.printTable();
        System.out.println();
    }

    private String[][] listToArray(List<List<String>> list) {
        String[][] outputArray = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {

            String[] row = new String[list.get(i).size()];
            outputArray[i] = list.get(i).toArray(row);
        }

        return outputArray;
    }
}
