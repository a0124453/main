package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UILogic {

    String input;
    
    @FXML
    TextField textInput;
    
    @FXML
    public void getInput() {
        input = textInput.getText();
        textInput.setText("");
    }
}
