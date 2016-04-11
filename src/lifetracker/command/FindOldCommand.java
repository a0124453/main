package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class FindOldCommand extends FindCommand {

    public FindOldCommand(boolean isOnlyToday) {
        this("", isOnlyToday);
    }

    public FindOldCommand(String searchTerm, boolean isOnlyToday) {
        super(searchTerm, isOnlyToday);
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        super.execute(calendar);

        CalendarList searchCalendar;

        if (getSearchTerm().isEmpty()) {
            searchCalendar = calendar.findArchivedByName("");
        } else {
            searchCalendar = calendar.findArchivedByName(getSearchTerm());
        }

        if (isOnlyToday()) {
            searchCalendar = searchCalendar.findToday();
        }

        return searchCalendar;
    }
}
