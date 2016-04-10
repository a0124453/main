package lifetracker.command;

import lifetracker.calendar.CalendarList;

//@@author A0091173J
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
            searchCalendar =  calendar.findAllByName(getSearchTerm());
        }

        if(isOnlyToday()){
            searchCalendar = searchCalendar.findToday();
        }

        return searchCalendar;
    }
}
