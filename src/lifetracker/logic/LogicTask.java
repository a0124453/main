package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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
    
    void setPeriod(Period period);
    
    Period getPeriod();

    int getLimitOccur();

    void setLimitOccur(int limitOccur);

    LocalDate getLimitDate();

    void setLimitDate(LocalDate limitDate);

    boolean isNew();

    void setNew(boolean isNew);
}
