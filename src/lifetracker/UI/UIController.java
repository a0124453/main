package lifetracker.UI;

import java.util.List;

import dnl.utils.text.table.TextTable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

public class UIController {

    Logic l;
    
    private static final String MESSAGE_WELCOME = "Welcome to the Life Tracker, Spend less time planning so you always know what's next.";
    private static final String MESSAGE_INPUT = "Command: ";

    private static final String[] EVENT_HEADERS = {"ID", "Name", "Start", "End"};
    private static final String[] TASK_HEADERS = {"ID", "Name", "Due"};

    private static final String EVENT_TITLE = "Events: ";
    private static final String TASK_TITLE = "Tasks: ";

    /*
    @FXML
    TextField textInput;
    @FXML
    TextArea textAreaOutput;
    @FXML
    Label labelFeedback;
    */
    
    public UIController(Logic l) {
        assert l != null;
        this.l = l;
    }

    @FXML
    public void getInput() {

        /*
        if (!textInput.getText().toLowerCase().equals("exit")) {
            //String userInput = textInput.getText();
            //execute(userInput);
            //textInput.setText("");
        } else {
            System.exit(0);
        }
        */
    }

    /*
    private void execute(String userInput) {
        assert l != null;
        
        ExecuteResult result;
        result = l.executeCommand(userInput);
        
        if (result.getType() == ExecuteResult.CommandType.DISPLAY) {

            //printTable(EVENT_TITLE, EVENT_HEADERS, result.getEventList());
            //printTable(TASK_TITLE, TASK_HEADERS, result.getTaskList());

        }
        
        //labelFeedback.setText(result.getComment());
        
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
    */
}
