package lifetracker.parser.syntax;

//@@author A0091173J
/**
 * The different types of Commands.
 */
public enum CommandClass {
    GENERIC,
    DEADLINE,
    EVENT,
    RECURRING_EVENT,
    RECURRING_EVENT_OCCURRENCES,
    RECURRING_EVENT_DATE,
    RECURRING_TASK,
    RECURRING_TASK_OCCURRENCES,
    RECURRING_TASK_DATE,
    RECURRING,
    RECURRING_OCCURRENCES,
    RECURRING_DATE,
    STOP_RECURRING
}
