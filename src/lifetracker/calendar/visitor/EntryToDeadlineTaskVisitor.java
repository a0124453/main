package lifetracker.calendar.visitor;

import lifetracker.calendar.CalendarEntry;
import lifetracker.calendar.CalendarProperty;
import lifetracker.calendar.DeadlineTask;
import lifetracker.calendar.Event;
import lifetracker.calendar.GenericEntry;
import lifetracker.calendar.RecurringEvent;
import lifetracker.calendar.RecurringTask;

import java.time.LocalDateTime;

public class EntryToDeadlineTaskVisitor implements EntryVisitor<EditedEntryPair> {

    private static final String ERROR_EMPTY_DEADLINE = "Task deadline cannot be empty!";

    private final String name;
    private final LocalDateTime deadline;
    private final boolean isConvertForced;

    public EntryToDeadlineTaskVisitor(String name, LocalDateTime deadline, boolean isConvertForced) {
        this.name = name;
        this.deadline = deadline;
        this.isConvertForced = isConvertForced;
    }

    @Override
    public EditedEntryPair visit(GenericEntry entry) {
        if (deadline == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_DEADLINE);
        }

        DeadlineTask deadlineTask = new DeadlineTask(entry.getName(), entry.getDateTime(CalendarProperty.END));

        return edit(entry, deadlineTask);
    }

    @Override
    public EditedEntryPair visit(DeadlineTask task) {
        DeadlineTask clone = new DeadlineTask(task);
        return edit(clone, task);
    }

    @Override
    public EditedEntryPair visit(RecurringTask task) {
        RecurringTask clone = new RecurringTask(task);
        return edit(clone, task);
    }

    @Override
    public EditedEntryPair visit(Event event) {
        Event clone = new Event(event);
        return edit(clone, event);
    }

    @Override
    public EditedEntryPair visit(RecurringEvent event) {
        RecurringEvent clone = new RecurringEvent(event);
        return edit(clone, event);
    }

    private EditedEntryPair edit(CalendarEntry clone, DeadlineTask task) {
        if (isConvertForced) {
            task = new DeadlineTask(task);
        }

        if(name != null && !name.isEmpty()){
            task.setName(name);
        }

        if (deadline!=null){
            task.setDateTime(CalendarProperty.END, deadline);
        }

        return new EditedEntryPair(clone, task);
    }
}
