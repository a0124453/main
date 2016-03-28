package lifetracker.UI;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lifetracker.logic.Logic;

public class UIController {

    String userInput;

    @FXML
    TextField textInput;
    
    @FXML
    TextArea textAreaOutput;
    
    public UIController(Logic programLogic) {
        // TODO Auto-generated constructor stub
    }

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
