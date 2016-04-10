package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

//@@author A0149467N

public class LogicEventImpl implements LogicEvent {

    private String name; //Name of the event
    private int id; //Id of the event
    private LocalDateTime start; //Start date and time of the event
    private LocalDateTime end; //End date and time of the event
    private boolean isOverdue; //Whether the event is overdue
    private boolean isDone; //Whether the event is done
    private Period period; //Period of the event
    private int limitOccur; //The occurrence limit for the recurring event
    private LocalDate limitDate; //The limit date for the recurring event
    private boolean isNew; //Whether the event is highlighted
    
    @Override
    public void setName(String name) {
        assert name != null;
        
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setId(int id) {
        assert id > 0;
        
        this.id = id;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void setStart(LocalDateTime start) {
        assert start != null;
        
        this.start = start;
    }
    
    @Override
    public LocalDateTime getStart() {
        return start;
    }
    
    @Override
    public void setEnd(LocalDateTime end) {
        assert end != null;
        
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
    public void setActive(boolean isDone) {
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

    //Auto-generated function
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LogicEventImpl))
            return false;

        LogicEventImpl that = (LogicEventImpl) o;

        if (getId() != that.getId())
            return false;
        if (getOverdue() != that.getOverdue())
            return false;
        if (isDone() != that.isDone())
            return false;
        if (getLimitOccur() != that.getLimitOccur())
            return false;
        if (isNew() != that.isNew())
            return false;
        if (!getName().equals(that.getName()))
            return false;
        if (getStart() != null ? !getStart().equals(that.getStart()) : that.getStart() != null)
            return false;
        if (getEnd() != null ? !getEnd().equals(that.getEnd()) : that.getEnd() != null)
            return false;
        if (getPeriod() != null ? !getPeriod().equals(that.getPeriod()) : that.getPeriod() != null)
            return false;
        return getLimitDate() != null ? getLimitDate().equals(that.getLimitDate()) : that.getLimitDate() == null;

    }

    //Auto-generated function
    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getId();
        result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        result = 31 * result + (getOverdue() ? 1 : 0);
        result = 31 * result + (isDone() ? 1 : 0);
        result = 31 * result + (getPeriod() != null ? getPeriod().hashCode() : 0);
        result = 31 * result + getLimitOccur();
        result = 31 * result + (getLimitDate() != null ? getLimitDate().hashCode() : 0);
        result = 31 * result + (isNew() ? 1 : 0);
        return result;
    }
}
