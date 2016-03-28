package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UILogic {

    String userInput;
    
    @FXML
    TextField textInput;
    
    @FXML
    public void getInput() {
        userInput = textInput.getText();
        System.out.println(userInput);
        textInput.setText("");
        textInput.setPromptText("Enter Command");
    }
}
