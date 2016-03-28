package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UIController {

    String userInput;

    @FXML
    TextField textInput;
    
    @FXML
    TextArea textAreaOutput;
    
    @FXML
    Label labelFeedback;

    @FXML
    public void getInput() {

        if (!textInput.getText().toLowerCase().equals("exit")) {
            userInput = textInput.getText();
            System.out.println(userInput);
            textInput.setText("");
        } else {
            System.exit(0);
        }
    }
}
