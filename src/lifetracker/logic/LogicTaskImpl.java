package lifetracker.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class LogicTaskImpl implements LogicTask {
    
    private String name;
    private int id;
    private LocalDateTime deadline;
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
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    @Override
    public LocalDateTime getDeadline() {
        return deadline;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LogicTaskImpl))
            return false;

        LogicTaskImpl logicTask = (LogicTaskImpl) o;

        if (getId() != logicTask.getId())
            return false;
        if (getOverdue() != logicTask.getOverdue())
            return false;
        if (isDone() != logicTask.isDone())
            return false;
        if (getLimitOccur() != logicTask.getLimitOccur())
            return false;
        if (isNew() != logicTask.isNew())
            return false;
        if (!getName().equals(logicTask.getName()))
            return false;
        if (getDeadline() != null ? !getDeadline().equals(logicTask.getDeadline()) : logicTask.getDeadline() != null)
            return false;
        if (getPeriod() != null ? !getPeriod().equals(logicTask.getPeriod()) : logicTask.getPeriod() != null)
            return false;
        return getLimitDate() != null ?
                getLimitDate().equals(logicTask.getLimitDate()) :
                logicTask.getLimitDate() == null;

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getId();
        result = 31 * result + (getDeadline() != null ? getDeadline().hashCode() : 0);
        result = 31 * result + (getOverdue() ? 1 : 0);
        result = 31 * result + (isDone() ? 1 : 0);
        result = 31 * result + (getPeriod() != null ? getPeriod().hashCode() : 0);
        result = 31 * result + getLimitOccur();
        result = 31 * result + (getLimitDate() != null ? getLimitDate().hashCode() : 0);
        result = 31 * result + (isNew() ? 1 : 0);
        return result;
    }
}
