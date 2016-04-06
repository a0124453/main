package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class LogicEventImpl implements LogicEvent {

    private String name;
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isOverdue;
    private boolean isDone;
    private Period period;
    private int limitOccur;
    private LocalDate limitDate;
    private boolean isNew;
    
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
    public void setPeriod(Period period) {
     this.period = period;   
    }
    
    @Override
    public Period getPeriod() {
        return period;
    }
    
    @Override
    public int getLimitOccur() {
        return limitOccur;
    }

    @Override
    public void setLimitOccur(int limitOccur) {
        this.limitOccur = limitOccur;
    }

    @Override
    public LocalDate getLimitDate() {
        return limitDate;
    }

    @Override
    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
