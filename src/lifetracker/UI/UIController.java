package lifetracker.UI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

public class UIController implements Initializable {
    
    private static Logic l;

    @FXML
    TextField textInput;
    
    @FXML
    Label labelFeedback;
    
    @FXML
    TableView<List<String>> table;
    
    @FXML
    TableColumn<List<String>, String> columnID;
    
    @FXML
    TableColumn<List<String>, String> columnTask;
    
    @FXML
    TableColumn<List<String>, String> columnTime;
    

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private ObservableList<List<String>> parseUserList() {
        ExecuteResult result;
        
        result = l.executeCommand("list");
        ObservableList<List<String>> list = FXCollections.observableArrayList();
        list.addAll(result.getTaskList());
        return list;
    }
}
