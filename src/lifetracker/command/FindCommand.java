package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class FindCommand extends CommandObject {

    private static final String MESSAGE_SEARCH_TERM = "Displaying entries with: \"%1$s\".";
    private static final String MESSAGE_SEARCH_ALL = "Displaying entries.";
    private static final String MESSAGE_ADDON_TODAY = " (Today's entries only)";

    private final String searchTerm;
    private final boolean isOnlyToday;

    private CalendarList originalCalendar;

    public FindCommand(boolean isOnlyToday) {
        this("", isOnlyToday);
    }

    public FindCommand(String searchTerm, boolean isOnlyToday) {
        this.isOnlyToday = isOnlyToday;
        this.searchTerm = searchTerm.trim();
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        originalCalendar = calendar;

        CalendarList searchCalendar;

        if (searchTerm.isEmpty()) {
            setComment(MESSAGE_SEARCH_ALL);
            searchCalendar = calendar;
        } else {
            setComment(String.format(MESSAGE_SEARCH_TERM, searchTerm));

            searchCalendar = calendar.findByName(searchTerm);
        }

        if (isOnlyToday) {
            searchCalendar = searchCalendar.findToday();
        }

        return searchCalendar;
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        setComment(MESSAGE_SEARCH_ALL);

        return originalCalendar;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public boolean isOnlyToday() {
        return isOnlyToday;
    }
}
