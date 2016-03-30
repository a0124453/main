package lifetracker.UI;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
