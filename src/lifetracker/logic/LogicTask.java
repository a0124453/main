package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0149467N

public interface LogicTask {

    //setter and getter for each variable
    void setName(String name);
    
    String getName();
    
    void setId(int id);
    
    int getId();
    
    void setDeadline(LocalDateTime deadline);
    
    LocalDateTime getDeadline();
    
    void setOverdue(boolean isOverdue);
    
    boolean getOverdue();
    
    void setActive(boolean isDone);
    
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
