package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public interface LogicEvent {
    
    void setName(String name);
    
    String getName();
    
    void setId(int id);
    
    int getId();
    
    void setStart(LocalDateTime start);
    
    LocalDateTime getStart();
    
    void setEnd(LocalDateTime end);
    
    LocalDateTime getEnd();
    
    void setOverdue(boolean isOverdue);
    
    boolean getOverdue();
    
    void setDone(boolean isDone);
    
    boolean isDone();
    
    void setPeriod(TemporalAmount period);
    
    TemporalAmount getPeriod();
}
