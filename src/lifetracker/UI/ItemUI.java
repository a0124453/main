package lifetracker.UI;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemUI {

   SimpleListProperty<String> item;
   private boolean isDone;

    public List<String> getItem() {
        return item.get();
    }

    public ItemUI(List<String> item) {
        super();
        ObservableList<String> observableList = FXCollections.observableArrayList(item);
        this.item = new SimpleListProperty<String>((ObservableList<String>) observableList);
        setDone();
    }
    
    private void setDone() {
        if(item.get(2).equals("true"))
            this.isDone = false;
        else
            this.isDone = true;
    }
    
    public boolean getDone() {
        return this.isDone;
    }
    

}
