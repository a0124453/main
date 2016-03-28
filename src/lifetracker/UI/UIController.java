package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

public class UIController {
    
    private static Logic l;

    @FXML
    TextField textInput;
    
    @FXML
    Label labelFeedback;

    @FXML
    public void getInput() {
        String userInput;

        if (!textInput.getText().toLowerCase().equals("exit")) {
            userInput = textInput.getText();
            process(userInput);
            textInput.setText("");
        } else {
            System.exit(0);
        }
    }

    private void process(String userInput) {
        ExecuteResult result;
        
        result = l.executeCommand(userInput);
        labelFeedback.setText(result.getComment());
    }

    public static Logic getLogic() {
        return l;
    }

    public static void setLogic(Logic l) {
        assert l != null;
        UIController.l = l;
    }
}
