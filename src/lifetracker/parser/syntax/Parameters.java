package lifetracker.parser.syntax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
//@@author A0091173J

/**
 * A data structure for storing parses parameters of a command.
 */
public class Parameters {
    public CommandClass commandClass;
    public String name;
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
    public Period recurringPeriod;
    public int occurLimit;
    public LocalDate dateLimit;
    public boolean isForcedOverwrite = false;
}
