package lifetracker.ui;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//@@author A0114240B-unused
//This class was replaced by LogicEvent and LogicTask.
public class ItemUI {

   SimpleListProperty<String> item;

    public List<String> getItem() {
        return item.get();
    }

    public ItemUI(List<String> item) {
        super();
        ObservableList<String> observableList = FXCollections.observableArrayList(item);
        this.item = new SimpleListProperty<String>((ObservableList<String>) observableList);
    }
}
