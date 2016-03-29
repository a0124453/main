package lifetracker.logic;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Task {

   SimpleListProperty<String> event;

    public List<String> getEvent() {
        return event.get();
    }

    public Task(List<String> event) {
        super();
        this.event = new SimpleListProperty<String>((ObservableList<String>) event);
    }
}
