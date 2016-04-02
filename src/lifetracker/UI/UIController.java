package lifetracker.UI;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

public class UIController implements Initializable {

    private static Logic l;

    @FXML Label labelTitle;
    @FXML
    TextField textInput;
    @FXML
    Label labelFeedback;
    @FXML
    TableView<ItemUI> tableTask;
    @FXML
    TableColumn<ItemUI, String> columnTaskID;
    @FXML
    TableColumn<ItemUI, String> columnTaskName;
    @FXML
    TableColumn<ItemUI, String> columnTaskActive;
    @FXML
    TableColumn<ItemUI, String> columnTaskTime;
    @FXML
    TableColumn<ItemUI, String> columnTaskRecurring;
    @FXML
    TableView<ItemUI> tableEvent;
    @FXML
    TableColumn<ItemUI, String> columnEventID;
    @FXML
    TableColumn<ItemUI, String> columnEventName;
    @FXML
    TableColumn<ItemUI, String> columnEventActive;
    @FXML
    TableColumn<ItemUI, String> columnEventStartTime;
    @FXML
    TableColumn<ItemUI, String> columnEventEndTime;
    @FXML
    TableColumn<ItemUI, String> columnEventRecurring;

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

        labelFeedback.setText(result.getComment());

        if (result.getType() == ExecuteResult.CommandType.DISPLAY) {
            populateList(result);
        }
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
        columnTaskID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(0)));
        
        columnTaskName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(1)));

        columnTaskActive.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(2)));

        columnTaskTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(3)));

        columnTaskRecurring.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(4)));

        columnEventID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(0)));

        columnEventName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(1)));

        columnEventActive.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(2)));

        columnEventStartTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(3)));

        columnEventEndTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(4)));

        columnEventRecurring.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(5)));
        
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

}
