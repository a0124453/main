package lifetracker.command;

import lifetracker.calendar.CalendarList;

public class FindOldCommand extends FindCommand{

    public FindOldCommand() {
    }

    public FindOldCommand(String searchTerm) {
        super(searchTerm);
    }

    @Override
    public CalendarList execute(CalendarList calendar) {
        super.execute(calendar);

        if (searchTerm.isEmpty()) {
            return calendar.findArchivedByName("");
        } else {
            return calendar.findArchivedByName(searchTerm);
        }
    }
}
