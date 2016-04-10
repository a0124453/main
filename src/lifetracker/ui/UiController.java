package lifetracker.ui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.util.Callback;
import lifetracker.LifeTracker;
import lifetracker.logic.ExecuteResult;
import lifetracker.logic.Logic;
import lifetracker.logic.LogicEvent;
import lifetracker.logic.LogicTask;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//@@author A0114240B
/**
 * This class is the main controller for the User Interface (UI) of LifeTracker.
 * The user interface design that this class is controlling can be found
 * inside the file /lifetracker/ui/UiDesign.fxml. All functions that relate
 * user action to the Logic Class can be found here.
 */
public class UiController implements Initializable {
    /** Variables used for logging */
    private static final Logger STORE_LOG = Logger.getGlobal();
    private static final String LOG_STARTUP = "UI: Starting";
    private static final String LOG_SHUTDOWN = "UI: Exiting";
    
    /** Constant variables to avoid magic number/string */
    private static final String PATH_README_CSS = "/lifetracker/ui/README.css";
    private static final String PATH_README_HTML = "/lifetracker/ui/README.html";
    private static final String FIELD_EMPTY = "";
    private static final String FIELD_DAY = "day(s)";
    private static final String FIELD_MONTH = "month(s)";
    private static final String FIELD_YEAR = "year(s)";
    private static final String FIELD_MINUTE = "minute(s)";
    private static final String FIELD_HOUR = "hour(s)";
    private static final String FIELD_LIMIT_OCCUR_SUFFIX = " time(s)";
    private static final String FIELD_LIMIT_OCCUR_PREFIX = " for ";
    private static final String FIELD_LIMIT_DATE = " until ";
    private static final PseudoClass PSEUDO_CLASS_OVERDUE = PseudoClass.getPseudoClass("overdue");
    private static final PseudoClass PSEUDO_CLASS_DONE = PseudoClass.getPseudoClass("done");
    private static final PseudoClass PSEUDO_CLASS_NEW = PseudoClass.getPseudoClass("new");

    private static Logic l;
    private static List<String> inputHistory;
    private static int inputHistoryIndex;
    private static ObservableList<LogicTask> taskList = FXCollections.observableArrayList();
    private static ObservableList<LogicEvent> eventList = FXCollections.observableArrayList();
    private static WebEngine webEngine;

    /** Variables linked to UiDesign.fxml */
    @FXML
    Label labelTitle;
    @FXML
    TextField textInput;
    @FXML
    Label labelFeedback;
    @FXML
    TableView<LogicTask> tableTask;
    @FXML
    TableColumn<LogicTask, String> columnTaskId;
    @FXML
    TableColumn<LogicTask, String> columnTaskName;
    @FXML
    TableColumn<LogicTask, String> columnTaskTime;
    @FXML
    TableColumn<LogicTask, String> columnTaskRecurring;
    @FXML
    TableView<LogicEvent> tableEvent;
    @FXML
    TableColumn<LogicEvent, String> columnEventId;
    @FXML
    TableColumn<LogicEvent, String> columnEventName;
    @FXML
    TableColumn<LogicEvent, String> columnEventStartTime;
    @FXML
    TableColumn<LogicEvent, String> columnEventEndTime;
    @FXML
    TableColumn<LogicEvent, String> columnEventRecurring;
    @FXML
    WebView webView;

    /**
     * Initialize all the variables that are linked to UIDesign.fxml.
     * These variables are linked to various UI components of LifeTracker.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLog();
        initTabBehaviour();
        initWebView();
        initInputHistory();
        initTableTask();
        initTableEvent();
        initTextInputKeyDetection();
        focusTextInput();
    }
    
    /**
     * Logging when the UI is launched
     */
    private void initLog() {
        STORE_LOG.log(Level.INFO, LOG_STARTUP);
    }

    /**
     * Get the input from <TextField> textInput when user press enter.
     * This method is linked to textInput component in the UI via UiDesign.fxml.
     */
    @FXML
    public void getInput() {
        String userInput = textInput.getText();
        addInputToHistory(userInput);
        process(userInput);
        textInput.setText(FIELD_EMPTY);
    }
    
    /**
     * Return Logic object.
     * The Logic object can never be null, thus assertion is used.
     * 
     * @return  Logic object.
     */
    public Logic getLogic() {
        assert l != null;
        return l;
    }

    /**
     * Set the Logic object that will be used in various functions under this class.
     * The Logic object can never be null, thus assertion is used.
     * 
     * @param l Logic object.
     */
    public void setLogic(Logic l) {
        assert l != null;
        UiController.l = l;
    }
    
    /**
     * Initialize how the behavior works when user press TAB button.
     * User can only switch from textInput to tableEvent to tableTask and not the
     * rest of the UI component.
     */
    private void initTabBehaviour() {
        textInput.setFocusTraversable(true);
        tableEvent.setFocusTraversable(true);
        tableTask.setFocusTraversable(true);
        webView.setFocusTraversable(true);
        labelFeedback.setFocusTraversable(false);
        labelTitle.setFocusTraversable(false);
    }
    
    /**
     * Initialize the webView and load it with README.html.
     * README.html can be found in /lifetracker/ui/README.html. The initial visibility
     * is set to false since webView is loaded behind tableTask and tableEvent.
     */
    private void initWebView() {
        String htmlURL = LifeTracker.class.getResource(PATH_README_HTML).toExternalForm();
        String cssURL = LifeTracker.class.getResource(PATH_README_CSS).toString();
        webEngine = webView.getEngine();
        webEngine.setUserStyleSheetLocation(cssURL);
        webEngine.load(htmlURL);
        webView.setVisible(false);
    }
    
    /**
     * Initialize the array inputHistory to keep track of the user input.
     */
    private void initInputHistory() {
        inputHistory = new ArrayList<String>();
        inputHistoryIndex = -1;
    }
    
    /**
     * Initialize tableTask.
     * This method initialize tableTask by indicating what field to be
     * put inside each cell.
     */
    private void initTableTask() {
        initColumnTaskId();
        initColumnTaskName();
        initColumnTaskTime();
        initColumnTaskRecurring();
        setTableTaskRowStyle();
        tableTask.setItems(taskList);
    }
    
    /**
     * Initialize tableEvent.
     * This method initialize tableEvent by indicating what field to be
     * put inside each cell.
     */
    private void initTableEvent() {
        initColumnEventId();
        initColumnEventName();
        initColumnEventStartTime();
        initColumnEventEndTime();
        initColumnEventRecurring();
        setTableEventRowStyle();
        tableEvent.setItems(eventList);
    }
    
    /**
     * Detect the key that are being released when the user is typing in textInput.
     */
    private void initTextInputKeyDetection() {
        textInput.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                processKeyCode(event);
            }
        });
    }
    
    /**
     * Set the focus on the textInput component of the UI with the caret.
     */
    private void focusTextInput() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textInput.requestFocus();
            }
        });
    }
    
    /**
     * Initialize the columnTaskId of the tableTask with the parameter from LogicTask object.
     */
    private void initColumnTaskId() {
        columnTaskId
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getId())));
    }
    
    /**
     * Initialize the columnTaskName of the tableTask with the parameter from LogicTask object.
     */
    private void initColumnTaskName() {
        columnTaskName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
    }
    
    /**
     * Initialize the columnTaskTime of the tableTask with the parameter from LogicTask object.
     */
    private void initColumnTaskTime() {
        columnTaskTime.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<LogicTask, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<LogicTask, String> param) {
                        return parseDeadlineFromLogicTask(param);
                    }
                });
    }
    
    /**
     * Initialize the columnTaskRecurring of the tableTask with the parameter from LogicTask object.
     */
    private void initColumnTaskRecurring() {
        columnTaskRecurring.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<LogicTask, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<LogicTask, String> param) {
                        return parsePeriodFromLogicTask(param);
                    }
                });
    }
    
    /**
     * Set the each individual row of the tableTask with an additional specific style.
     * This is done through the creation of new private class called TableTaskRowWithStyles.
     * There are three additional styles:
     * 1. strike-through text for the row with a task that is done.
     * 2. red background for the row with a task that is overdue (passed its deadline).
     * 3. green background for the row with a task that is newly created.
     */
    private void setTableTaskRowStyle() {
        tableTask.setRowFactory(new Callback<TableView<LogicTask>, TableRow<LogicTask>>() {
            @Override
            public TableRow<LogicTask> call(TableView<LogicTask> tableEventView) {
                
                return new TableTaskRowWithStyles();
            }
        });
    }
    
    /**
     * Initialize the columnEventId of the tableEvent with the parameter from LogicEvent object.
     */
    private void initColumnEventId() {
        columnEventId
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getId())));
    }

    /**
     * Initialize the columnEventName of the tableEvent with the parameter from LogicEvent object.
     */
    private void initColumnEventName() {
        columnEventName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getName()));
    }

    /**
     * Initialize the columnEventStartTime of the tableEvent with the parameter from LogicEvent object.
     */
    private void initColumnEventStartTime() {
        columnEventStartTime.setCellValueFactory(
                param -> new ReadOnlyStringWrapper(convertDateTimeToString(param.getValue().getStart())));
    }
    
    /**
     * Initialize the columnEventEndTime of the tableEvent with the parameter from LogicEvent object.
     */
    private void initColumnEventEndTime() {
        columnEventEndTime.setCellValueFactory(
                param -> new ReadOnlyStringWrapper(convertDateTimeToString(param.getValue().getEnd())));
    }
    
    /**
     * Initialize the columnEventRecurring of the tableEvent with the parameter from LogicEvent object.
     */
    private void initColumnEventRecurring() {
        columnEventRecurring.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<LogicEvent, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<LogicEvent, String> param) {
                        return parsePeriodFromLogicEvent(param);
                    }
                });
    }

    /**
     * Set the each individual row of the tableEvent with an additional specific style.
     * This is done through the creation of new private class called TableEventRowWithStyles.
     * There are three additional styles:
     * 1. strike-through text for the row with a task that is done.
     * 2. red background for the row with a task that is overdue (passed its deadline).
     * 3. green background for the row with a task that is newly created.
     */
    private void setTableEventRowStyle() {
        tableEvent.setRowFactory(new Callback<TableView<LogicEvent>, TableRow<LogicEvent>>() {
            @Override
            public TableRow<LogicEvent> call(TableView<LogicEvent> tableEventView) {
                return new TableEventRowWithStyles();
            }
        });
    }
    
    private void addInputToHistory(String userInput) {
        if(!inputHistory.isEmpty()){ 
            if (!isRepeatedInput(userInput)) {
                storeInputToHistory(userInput);
            }
        } else {
            storeInputToHistory(userInput);
        }

    }
    
    private boolean isRepeatedInput(String userInput) {
        String prevInput = inputHistory.get(inputHistory.size() - 1); 
        boolean isRepeated = prevInput.equals(userInput);
        return isRepeated;
    }

    private void storeInputToHistory(String userInput) {
        inputHistory.add(userInput);
        inputHistoryIndex = inputHistory.size();
    }

    private void process(String userInput) {
        ExecuteResult result = l.executeCommand(userInput);
        ExecuteResult.CommandType commnadType = result.getType();
        String comment = result.getComment();
        processCommandType(result, commnadType, comment);
    }

    private void processCommandType(ExecuteResult result, ExecuteResult.CommandType commnadType, String comment) {
        switch (commnadType) {
        case HELP :
            showWebView();
            break;
        case EXIT :
            processExit();
            break;
        case DISPLAY :
            hideWebView();
            labelFeedback.setText(comment);
            populateList(result);
            break;
        default :
            hideWebView();
            labelFeedback.setText(comment);
            break;
        }
    }

    private void processExit() {
        STORE_LOG.log(Level.INFO, LOG_SHUTDOWN);
        Platform.exit();
    }

    private void hideWebView() {
        webView.setVisible(false);
        webView.toBack();
        tableEvent.setVisible(true);
        tableTask.setVisible(true);
    }

    private void showWebView() {
        webView.setVisible(true);
        webView.toFront();
        tableEvent.setVisible(false);
        tableTask.setVisible(false);
    }

    private void processKeyCode(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        switch (keyCode) {
        case UP :
            processUpKey();
            break;
        case DOWN :
            processDownKey();
            break;
        default :
            break;
        }
    }

    private void processUpKey() {
        if (inputHistoryIndex > 0) {
            inputHistoryIndex--;
            setTextInputHistory();
            setEndCaretPosition();
        }
    }

    private void processDownKey() {
        if (inputHistoryIndex < inputHistory.size() - 1) {
            inputHistoryIndex++;
            setTextInputHistory();
            setEndCaretPosition();
        } else if (inputHistoryIndex == inputHistory.size() - 1) {
            inputHistoryIndex++;
            textInput.setText(FIELD_EMPTY);
        }
    }

    private void setEndCaretPosition() {
        textInput.positionCaret(textInput.getText().length());
    }

    private void setTextInputHistory() {
        textInput.setText(inputHistory.get(inputHistoryIndex));
    }

    private ObservableValue<String> parsePeriodFromLogicEvent(CellDataFeatures<LogicEvent, String> param) {
        LocalDate limitDate = param.getValue().getLimitDate();
        int limitOccur = param.getValue().getLimitOccur();
        String periodString = convertTemporalToString(param.getValue().getPeriod());
        periodString += parseLimit(limitDate, limitOccur);
        
        return new ReadOnlyStringWrapper(periodString);
    }

    private ObservableValue<String> parsePeriodFromLogicTask(CellDataFeatures<LogicTask, String> param) {
        LocalDate limitDate = param.getValue().getLimitDate();
        int limitOccur = param.getValue().getLimitOccur();
        String periodString = convertTemporalToString(param.getValue().getPeriod());
        periodString += parseLimit(limitDate, limitOccur);
        
        return new ReadOnlyStringWrapper(periodString);
    }

    private String parseLimit(LocalDate limitDate, int limitOccur) {
        String limit = FIELD_EMPTY;
        if (limitOccur > 0) {
            limit = parseLimitOccur(limitOccur);
        } else if (limitDate != null) {
            limit = parseLimitDate(limitDate);
        }
        
        return limit;
    }

    private String parseLimitDate(LocalDate limitDate) {
        String parse = FIELD_LIMIT_DATE + convertDateToString(limitDate);
        
        return parse;
    }

    private String parseLimitOccur(int limitOccur) {
        String parse = FIELD_LIMIT_OCCUR_PREFIX + limitOccur + FIELD_LIMIT_OCCUR_SUFFIX;
        
        return parse;
    }

    private String convertDateToString(LocalDate limitDate) {
        String dateString = limitDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        
        return dateString;
    }

    private ObservableValue<String> parseDeadlineFromLogicTask(CellDataFeatures<LogicTask, String> param) {
        LocalDateTime deadline = param.getValue().getDeadline();
        String deadlineString = convertDeadlineToString(deadline);
        
        return new ReadOnlyStringWrapper(deadlineString);
    }

    private String convertDeadlineToString(LocalDateTime deadline) {
        String deadlineString;
        if (deadline != null) {
            deadlineString = convertDateTimeToString(deadline);
        } else {
            deadlineString = FIELD_EMPTY;
        }
        
        return deadlineString;
    }

    private String convertDateTimeToString(LocalDateTime deadline) {
        String dateTimeString = deadline.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        
        return dateTimeString;
    }

    private String convertTemporalToString(TemporalAmount period) {
        String periodString;
        if (period == null) {
            periodString = FIELD_EMPTY;
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
        String periodString = formatDuration(years, FIELD_YEAR) + formatDuration(months, FIELD_MONTH)
                + formatDuration(days, FIELD_DAY);

        return periodString;
    }

    private String convertDurationToString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        String durationString = formatDuration(hours, FIELD_HOUR) + formatDuration(minutes, FIELD_MINUTE);
        
        return durationString;
    }

    private String formatDuration(long duration, String label) {
        return duration == 0 ? FIELD_EMPTY : duration + " " + label + " ";
    }

    public void populateList(ExecuteResult result) {
        populateTaskList(result);
        populateEventList(result);
    }

    private void populateEventList(ExecuteResult result) {
        eventList.clear();
        for (LogicEvent event : result.getEventList()) {
            eventList.add(event);
        }
    }

    private void populateTaskList(ExecuteResult result) {
        taskList.clear();
        for (LogicTask task : result.getTaskList()) {
            taskList.add(task);
        }
    }

    private class TableEventRowWithStyles extends TableRow<LogicEvent> {
        @Override
        protected void updateItem(LogicEvent event, boolean b) {
            super.updateItem(event, b);
            boolean overdue = (event != null) && (event.getOverdue());
            boolean newEvent = (event != null) && (event.isNew());
            boolean done = (event != null) && (!event.isDone());
            setOverdueStyle(overdue, newEvent, done);
            setDoneStyle(event, b, done);
            setNewStyle(event, b, newEvent);
        }

        private void setNewStyle(LogicEvent event, boolean b, boolean newEvent) {
            super.updateItem(event, b);
            pseudoClassStateChanged(PSEUDO_CLASS_NEW, newEvent);
        }

        private void setDoneStyle(LogicEvent event, boolean b, boolean done) {
            super.updateItem(event, b);
            pseudoClassStateChanged(PSEUDO_CLASS_DONE, done);
        }

        private void setOverdueStyle(boolean overdue, boolean newEvent, boolean done) {
            if (!done && !newEvent) {
                pseudoClassStateChanged(PSEUDO_CLASS_OVERDUE, overdue);
            }
        }
    }

    private class TableTaskRowWithStyles extends TableRow<LogicTask> {
        @Override
        protected void updateItem(LogicTask task, boolean b) {
            super.updateItem(task, b);
            boolean overdue = (task != null) && (task.getOverdue());
            boolean newTask = (task != null) && (task.isNew());
            boolean done = (task != null) && (!task.isDone());
            setOverdueStyle(overdue, newTask, done);
            setDoneStyle(task, b, done);
            setNewStyle(task, b, newTask);
        }
        
        private void setNewStyle(LogicTask event, boolean b, boolean newEvent) {
            super.updateItem(event, b);
            pseudoClassStateChanged(PSEUDO_CLASS_NEW, newEvent);
        }

        private void setDoneStyle(LogicTask task, boolean b, boolean done) {
            super.updateItem(task, b);
            pseudoClassStateChanged(PSEUDO_CLASS_DONE, done);
        }

        private void setOverdueStyle(boolean overdue, boolean newTask, boolean done) {
            if (!done && !newTask) {
                pseudoClassStateChanged(PSEUDO_CLASS_OVERDUE, overdue);
            }
        }
    }

}
