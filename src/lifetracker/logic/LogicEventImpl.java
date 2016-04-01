package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public class LogicEventImpl implements LogicEvent {

    private String name;
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isOverdue;
    private boolean isDone;
    private TemporalAmount period;
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    
    @Override
    public LocalDateTime getStart() {
        return start;
    }
    
    @Override
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    
    @Override
    public LocalDateTime getEnd() {
        return end;
    }
    
    @Override
    public void setOverdue(boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    
    @Override
    public boolean getOverdue() {
        return isOverdue;
    }
    
    @Override
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
    
    @Override
    public boolean isDone() {
        return isDone;
    }
    
    @Override
    public void setPeriod(TemporalAmount period) {
     this.period = period;   
    }
    
    @Override
    public TemporalAmount getPeriod() {
        return period;
    }
}
