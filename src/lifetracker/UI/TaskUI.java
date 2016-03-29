package lifetracker.UI;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskUI {

   SimpleListProperty<String> task;

    public List<String> getTask() {
        return task.get();
    }

    public TaskUI(List<String> task) {
        super();
        ObservableList<String> observableList = FXCollections.observableArrayList(task);
        this.task = new SimpleListProperty<String>((ObservableList<String>) observableList);
    }
}
