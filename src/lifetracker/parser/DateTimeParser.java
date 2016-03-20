package lifetracker.parser;

import com.joestelmach.natty.DateGroup;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//@@author A0091173J
/**
 * A parser that parses DateTime strings.
 * <p>
 * This class uses natty to parse date and time strings. After parsing, the datetimes are adjusted accordingly to the
 * rules below.
 * <p>
 * Single DateTime:
 * <ul>
 * <li>If original string did not specify time, time is set to 2359
 * <li>If original string did not specify date, date is set to the next date where the DateTime is in the future.
 * </ul>
 */
class DateTimeParser {
    private static DateTimeParser instance = new DateTimeParser();

    static DateTimeParser getInstance() {
        return instance;
    }

    private static final String EMPTY_DATE_DEFAULT_STRING = "today";
    private static final String NATTY_TIME_FIELD = "explicit_time";
    private static final String NATTY_DATE_FIELD = "date";

    private final com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();

    private DateTimeParser() {
    }

    boolean isDateTime(String dateTimeString) {
        assert dateTimeString != null;

        return dateTimeString.isEmpty() || nattyParser.parse(dateTimeString).size() == 1;
    }

    LocalDateTime parseSingleDateTime(String dateTimeString) {
        assert isDateTime(dateTimeString);

        dateTimeString = fillEmpty(dateTimeString);

        DateGroup parsedDateGroup = parseWithNatty(dateTimeString);

        LocalDateTime parsedDateTimeObj = convertDateGroupToLocalDateTime(parsedDateGroup);

        return adjustSingleDateToDefault(parsedDateTimeObj, parsedDateGroup.getParseLocations().keySet());
    }

    List<LocalDateTime> parseDoubleDateTime(String startString, String endString) {
        assert isDateTime(startString);
        assert isDateTime(endString);

        List<LocalDateTime> dateTimeResults = new ArrayList<>(2);

        startString = fillEmpty(startString);
        endString = fillEmpty(endString);

        DateGroup startDateGroup = parseWithNatty(startString);
        DateGroup endDateGroup = parseWithNatty(endString);

        LocalDateTime startDateTime = convertDateGroupToLocalDateTime(startDateGroup);
        LocalDateTime endDateTime = convertDateGroupToLocalDateTime(endDateGroup);

        dateTimeResults.add(startDateTime);
        dateTimeResults.add(endDateTime);

        return dateTimeResults;
    }

    private String fillEmpty(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return EMPTY_DATE_DEFAULT_STRING;
        } else {
            return dateTimeString;
        }
    }

    private DateGroup parseWithNatty(String dateTimeString) {
        return nattyParser.parse(dateTimeString).get(0);
    }

    private LocalDateTime convertDateGroupToLocalDateTime(DateGroup dateGroup) {
        Date date = dateGroup.getDates().get(0);

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime adjustSingleDateToDefault(LocalDateTime dateTime, Set<String> parseElements) {
        LocalDateTime adjustedDateTime = dateTime;

        if (!parseElements.contains(NATTY_TIME_FIELD)) {
            adjustedDateTime = LocalDateTime.of(adjustedDateTime.toLocalDate(), LocalTime.MIDNIGHT.minusMinutes(1));
        }

        //Natty defaults to current date without explicitly stated date
        if (!parseElements.contains(NATTY_DATE_FIELD)
                && adjustedDateTime.isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
            adjustedDateTime = adjustedDateTime.plusDays(1);
        }

        return adjustedDateTime;
    }
}
