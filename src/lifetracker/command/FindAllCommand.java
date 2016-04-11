package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J

/**
 * A {@code CommandObject} that finds all entries, both archived or unarchived in the calendar.
 * <p>
 * This class processes the query and returns the results in a new calendar. Undoing this command simply returns the
 * same calendar that execute was called with.
 */
public class FindAllCommand extends FindCommand {
    public FindAllCommand(boolean isOnlyToday) {
        this("", isOnlyToday);
    }

    public FindAllCommand(String searchTerm, boolean isOnlyToday) {
        super(searchTerm, isOnlyToday);
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        super.execute(calendar);

        CalendarList searchCalendar;

        if (getSearchTerm().isEmpty()) {
            searchCalendar = calendar.findAllByName("");
        } else {
            searchCalendar = calendar.findAllByName(getSearchTerm());
        }

        if (isOnlyToday()) {
            searchCalendar = searchCalendar.findToday();
        }

        return searchCalendar;
    }
}
