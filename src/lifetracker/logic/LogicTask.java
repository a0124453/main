package lifetracker.logic;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public interface LogicTask {

    void setName(String name);
    
    String getName();
    
    void setId(int id);
    
    int getId();
    
    void setDeadline(LocalDateTime deadline);
    
    LocalDateTime getDeadline();
    
    void setOverdue(boolean isOverdue);
    
    boolean getOverdue();
    
    void setDone(boolean isDone);
    
    boolean isDone();
    
    void setPeriod(TemporalAmount period);
    
    TemporalAmount getPeriod();

    int getLimitOccur();

    void setLimitOccur(int limitOccur);

    LocalDateTime getLimitDate();

    void setLimitDate(LocalDateTime limitDate);

    boolean isNew();

    void setNew(boolean isNew);
}
