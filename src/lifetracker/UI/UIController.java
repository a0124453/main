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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicEvent;
import lifetracker.logic.LogicTask;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAmount;
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
    TableView<LogicTask> tableTask;
    @FXML
    
    TableColumn<LogicTask, String> columnTaskID;
    @FXML
    TableColumn<LogicTask, String> columnTaskName;
    @FXML
    TableColumn<LogicTask, String> columnTaskTime;
    @FXML
    TableColumn<LogicTask, String> columnTaskRecurring;
    @FXML
    
    TableView<LogicEvent> tableEvent;
    @FXML
    TableColumn<LogicEvent, String> columnEventID;
    @FXML
    TableColumn<LogicEvent, String> columnEventName;
    @FXML
    TableColumn<LogicEvent, String> columnEventActive;
    @FXML
    TableColumn<LogicEvent, String> columnEventStartTime;
    @FXML
    TableColumn<LogicEvent, String> columnEventEndTime;
    @FXML
    TableColumn<LogicEvent, String> columnEventRecurring;

    private static ObservableList<LogicTask> taskList = FXCollections.observableArrayList();
    private static ObservableList<LogicEvent> eventList = FXCollections.observableArrayList();

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
        columnTaskID.setCellValueFactory(param -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getId())));
        
        columnTaskName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));

        //columnTaskActive.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(2)));

        //columnTaskTime.setCellValueFactory(param -> new ReadOnlyStringWrapper((param.getValue().getDeadline().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)).toString())));
        columnTaskTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LogicTask,String>, ObservableValue<String>>() {
            
            @Override
            public ObservableValue<String> call(CellDataFeatures<LogicTask, String> param) {
                LocalDateTime deadline = param.getValue().getDeadline();
                String deadlineString;
                if(deadline != null) {
                   deadlineString = deadline.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
                } else {
                    deadlineString = "";
                }
                
                return new ReadOnlyStringWrapper(deadlineString);
            }
        });
        //columnTaskRecurring.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(4)));
        columnTaskRecurring.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LogicTask,String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<LogicTask, String> param) {
                TemporalAmount period = param.getValue().getPeriod();
                String periodString;
                if (period == null) {
                    periodString = "";
                } else if (period instanceof Period) {
                    periodString = convertPeriodToString(((Period) period).normalized());
                }
                return null;
            }
        });
        //columnEventID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(0)));

        //columnEventName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(1)));

        //columnEventActive.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(2)));

        //columnEventStartTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(3)));

        //columnEventEndTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(4)));

        //columnEventRecurring.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(5)));
        
        tableTask.setItems(taskList);
        tableEvent.setItems(eventList);
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                textInput.requestFocus();
            }
        });
    }
    
    private String convertPeriodToString(Period period) {
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return formatDuration(years, YEAR_FIELD) + formatDuration(months, MONTH_FIELD) + formatDuration(days, DAY_FIELD);
    }
    

    public static void populateList(ExecuteResult result) {
        taskList.clear();
        for (LogicTask task : result.getTaskList()) {
            taskList.add(task);
        }
        eventList.clear();
    }

}
