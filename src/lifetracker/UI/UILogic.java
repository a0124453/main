package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UILogic {

    String userInput;

    @FXML
    TextField textInput;
    
    @FXML
    TextArea textAreaOutput;

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
