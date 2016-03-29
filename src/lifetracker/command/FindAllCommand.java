package lifetracker.command;

import lifetracker.calendar.CalendarList;

import java.time.LocalDate;
import java.time.LocalTime;

//@@author A0091173J
public class FindAllCommand extends FindCommand {
    public FindAllCommand() {
    }

    public FindAllCommand(String searchTerm) {
        super(searchTerm);
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        super.execute(calendar);

        String searchTerm = getSearchTerm();

        if (searchTerm.isEmpty()) {
            return calendar;
        } else {
            return calendar.findAll(getSearchTerm(), LocalDate.MIN, LocalTime.MIN, LocalDate.MAX, LocalTime.MAX);
        }
    }
}
