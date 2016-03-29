package lifetracker.UI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;
import lifetracker.logic.Task;

public class UIController implements Initializable {
    
    private static Logic l;

    @FXML TextField textInput;
    @FXML Label labelFeedback;
    @FXML TableView<Task> table;
    @FXML TableColumn<Task, String> columnID;
    @FXML TableColumn<Task, String> columnTask; 
    @FXML TableColumn<Task, String> columnTime;
    
    public ObservableList<String> task = FXCollections.observableArrayList("1","dinner", "7pm");
    public ObservableList<Task> list = FXCollections.observableArrayList(
            new Task(task)
            );

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
        columnID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task,String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTask().get(0));
            }
        });
        
        columnTask.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task,String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTask().get(1));
            }
        });
        
        columnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task,String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTask().get(2));
            }
        });
        
        table.setItems(list);
    }

    //private ObservableList<List<String>> parseUserList() {
        //ExecuteResult result;
        
        //result = l.executeCommand("list");
        //ObservableList<List<String>> list = FXCollections.observableArrayList();
        //list.addAll(result.getTaskList());
        //return list;
    //}
}
