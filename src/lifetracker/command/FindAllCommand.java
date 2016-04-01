package lifetracker.command;

import lifetracker.calendar.CalendarList;

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
            return calendar.findAll(null,null,null,null,null);
        } else {
            return calendar.findAll(getSearchTerm(), null, null, null, null);
        }
    }
}
