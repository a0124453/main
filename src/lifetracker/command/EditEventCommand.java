package lifetracker.command;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarList;
import lifetracker.calendar.CalendarProperty;

import java.time.LocalDateTime;

//@@author A0091173J
public class EditEventCommand extends EditDeadlineTaskCommand {

    final LocalDateTime startDateTime;

    public EditEventCommand(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isForcedConvert) {
        super(id, name, endDateTime, isForcedConvert);
        this.startDateTime = startDateTime;
    }

    @Override
    public CalendarList execute(CalendarList calendar) {

        if(isForcedConvert){
            oldEntry = calendar.updateToEvent(id, name, startDateTime, endDateTime, true);
        } else{
            CalendarEntry entryToEdit = calendar.get(id);

            if(entryToEdit.isProperty(CalendarProperty.RECURRING)){
                oldEntry = calendar.updateToRecurringEvent(id, name, startDateTime, endDateTime, null, true);
            } else{
                oldEntry = calendar.updateToEvent(id, name, startDateTime, endDateTime, true);
            }
        }

        setExecuted(true);
        setComment(String.format(MESSAGE_EDITED, id));

        return calendar;
    }
}
