package lifetracker.UI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
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
import lifetracker.logic.ExecuteResult.CommandType;
import lifetracker.logic.Logic;

public class UIController implements Initializable {

    private static Logic l;

    @FXML TextField textInput;
    @FXML Label labelFeedback;
    @FXML TableView<ItemUI> tableTask;
    @FXML TableColumn<ItemUI, String> columnTaskID;
    @FXML TableColumn<ItemUI, String> columnTaskName;
    @FXML TableColumn<ItemUI, String> columnTaskTime;
    @FXML TableView<ItemUI> tableEvent;
    @FXML TableColumn<ItemUI, String> columnEventID;
    @FXML TableColumn<ItemUI, String> columnEventName;
    @FXML TableColumn<ItemUI, String> columnEventStartTime;
    @FXML TableColumn<ItemUI, String> columnEventEndTime;
    
    private static ObservableList<ItemUI> taskList = FXCollections.observableArrayList();
    private static ObservableList<ItemUI> eventList = FXCollections.observableArrayList();
    
    @FXML
    public void getInput() {
        String userInput;

        userInput = textInput.getText();
        process(userInput);
        textInput.setText("");

    }

    private void process(String userInput) {
        ExecuteResult result;
        result = l.executeCommand(userInput);
        
        if (result.getType() == ExecuteResult.CommandType.EXIT) {
            Platform.exit();
        }

        if (result.getType() == ExecuteResult.CommandType.DISPLAY) {
            populateList(result);
        }
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
        columnTaskID.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(0));
                    }
                });

        columnTaskName.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(1));
                    }
                });

        columnTaskTime.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(2));
                    }
                });      
        
        columnEventID.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(0));
                    }
                });
        
        columnEventName.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(1));
                    }
                });
        
        columnEventStartTime.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(2));
                    }
                }); 
        
        columnEventEndTime.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ItemUI, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ItemUI, String> param) {
                        return new ReadOnlyStringWrapper(param.getValue().getItem().get(3));
                    }
                });
                
        tableTask.setItems(taskList);
        tableEvent.setItems(eventList);
    }

    public static void populateList(ExecuteResult result) {
        taskList.clear();

        for (List<String> task : result.getTaskList()) {
            taskList.add(new ItemUI(task));
        }

        eventList.clear();
        for (List<String> event : result.getEventList()) {
            eventList.add(new ItemUI(event));
        }
        
    }
    

    /*
     * private ObservableList<Task> parseUserList() { ExecuteResult result;
     * 
     * result = l.executeCommand("list"); return
     * FXCollections.observableArrayList(new Task(this.task)); }
     */
}
