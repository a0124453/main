package lifetracker.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class Parameters {
    public CommandClass commandClass;
    public String name;
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
    public Period recurringPeriod;
    public int occurLimit;
    public LocalDate dateLimit;
}
