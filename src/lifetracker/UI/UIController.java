package lifetracker.UI;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.util.Callback;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicEvent;
import lifetracker.logic.LogicTask;

import java.net.URL;
import java.time.Duration;
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
    private static final String DAY_FIELD = "day(s)";
    private static final String MONTH_FIELD = "month(s)";
    private static final String YEAR_FIELD = "year(s)";

    private static final String MINUTE_FIELD = "minute(s)";
    private static final String HOUR_FIELD = "hour(s)";

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
                String periodString = convertTemporalToString(param.getValue().getPeriod());
                return new ReadOnlyStringWrapper(periodString);
            }
        });
        columnEventID.setCellValueFactory(param -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getId())));

        columnEventName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));

        //columnEventActive.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(2)));

        columnEventStartTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))));
        
        columnEventEndTime.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))));

        //columnEventRecurring.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getItem().get(5)));
        columnEventRecurring.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LogicEvent,String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<LogicEvent, String> param) {
                String periodString = convertTemporalToString(param.getValue().getPeriod());
                return new ReadOnlyStringWrapper(periodString);
            }
        });
        
        final PseudoClass overduePseudoClass = PseudoClass.getPseudoClass("overdue");
        final PseudoClass donePseudoClass = PseudoClass.getPseudoClass("done");
        tableEvent.setRowFactory(new Callback<TableView<LogicEvent>, TableRow<LogicEvent>>() {
                    @Override
                    public TableRow<LogicEvent> call(TableView<LogicEvent> tableEventView) {
                        return new TableRow<LogicEvent>() {
                            @Override
                            protected void updateItem(LogicEvent event, boolean b) {
                                super.updateItem(event, b);
                                boolean overdue = event != null && event.getOverdue();
                                boolean done = event != null && !event.isDone();
                                
                                if(!done) {
                                    pseudoClassStateChanged(overduePseudoClass, overdue);
                                }
                                
                                super.updateItem(event, b);
                                pseudoClassStateChanged(donePseudoClass, done);
                            }


                        };
                    }
                });
        
        
        tableTask.setItems(taskList);
        tableEvent.setItems(eventList);
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                textInput.requestFocus();
            }
        });
    }
    
    private String convertTemporalToString(TemporalAmount period) {
        String periodString;
        if (period == null) {
            periodString = "";
        } else if (period instanceof Period) {
            periodString = convertPeriodToString(((Period) period).normalized());
        } else {
            periodString = convertDurationToString((Duration) period);
        }
        return periodString;
    }
    
    private String convertPeriodToString(Period period) {
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        return formatDuration(years, YEAR_FIELD) + formatDuration(months, MONTH_FIELD) + formatDuration(days, DAY_FIELD);
    }
    
    private String convertDurationToString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return formatDuration(hours, HOUR_FIELD) + formatDuration(minutes, MINUTE_FIELD);
    }
    
    private String formatDuration(long duration, String label) {
        return duration == 0 ? "" : duration + " " + label + " ";
    }

    public static void populateList(ExecuteResult result) {
        taskList.clear();
        for (LogicTask task : result.getTaskList()) {
            taskList.add(task);
        }
        eventList.clear();
        for (LogicEvent event : result.getEventList()) {
            eventList.add(event);
        }
    }

}
