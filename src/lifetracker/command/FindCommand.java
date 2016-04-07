package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J
public class FindCommand extends CommandObject {

    private static final String MESSAGE_SEARCH_TERM = "Displaying entries with: \"%1$s\".";
    private static final String MESSAGE_SEARCH_ALL = "Displaying all entries.";

    final String searchTerm;

    private CalendarList originalCalendar;

    public FindCommand() {
        this("");
    }

    public FindCommand(String searchTerm) {
        this.searchTerm = searchTerm.trim();
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        originalCalendar = calendar;

        if (searchTerm.isEmpty()) {
            setComment(MESSAGE_SEARCH_ALL);
            return calendar;
        } else {
            setComment(String.format(MESSAGE_SEARCH_TERM, searchTerm));

            return calendar.findByName(searchTerm);
        }
    }

    @Override
    public CalendarList undo(CalendarList calendar) {
        setComment(MESSAGE_SEARCH_ALL);

        return originalCalendar;
    }
}
