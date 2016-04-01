package lifetracker.logic;

public class LogicFloatingImpl implements LogicFloating {

    private String name;
    private int id;
    private boolean isDone;
    
    public void setName(String recordName) {
        this.name = recordName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setId(int recordId) {
        this.id = recordId;
    }
    
    public int getId() {
        return id;
    }
    
    public void setDone(boolean recordDone) {
        this.isDone = recordDone;
    }
    
    public boolean isDone() {
        return isDone;
    }
}