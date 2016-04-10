package lifetracker.parser.datetime;

import com.joestelmach.natty.DateGroup;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@@author A0091173J

/**
 * A parser that parses DateTime strings.
 * <p>
 * This class uses natty to parse date and time strings. After parsing, the
 * datetimes are adjusted accordingly to the rules below.
 * <p>
 * Single DateTime:
 * <ul>
 * <li>If original string did not specify time, time is set to 2359
 * <li>If original string did not specify date, date is set to the next date
 * where the DateTime is in the future.
 * </ul>
 */
public class DateTimeParser {
    private static final String EMPTY_DATE_DEFAULT_STRING = "today";
    private static final String NATTY_TIME_FIELD = "explicit_time";
    private static final String NATTY_DATE_FIELD = "date";
    private static final String NATTY_MERIDIAN_INDICATOR = "meridian_indicator";

    //Natty parses "1" as 1am, but "12" as 12pm so there's no need to adjust
    private static final LocalTime UNGODLY_HOUR_START = LocalTime.of(0, 59, 59);
    private static final LocalTime UNGODLY_HOUR_END = LocalTime.of(7, 0);

    private static DateTimeParser instance = new DateTimeParser();
    private final com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();

    public static DateTimeParser getInstance() {
        return instance;
    }

    private DateTimeParser() {
    }

    public boolean isDateTime(String dateTimeString) {
        assert dateTimeString != null;

        return dateTimeString.isEmpty() || nattyParser.parse(dateTimeString).size() == 1;
    }

    public LocalDateTime parseSingleDateTime(String dateTimeString) {
        assert isDateTime(dateTimeString);

        String filledDateTimeString = fillEmpty(dateTimeString);

        DateGroup parsedDateGroup = parseWithNatty(filledDateTimeString);

        LocalDateTime parsedDateTimeObj = convertDateGroupToLocalDateTime(parsedDateGroup);

        return adjustSingleDateToDefault(parsedDateTimeObj, parsedDateGroup.getParseLocations().keySet());
    }

    public List<LocalDateTime> parseDoubleDateTime(String startString, String endString) {
        assert isDateTime(startString);
        assert isDateTime(endString);

        List<LocalDateTime> dateTimeResults = new ArrayList<>(2);

        boolean isEndEmpty = StringUtils.isBlank(endString);
        boolean isStartEmpty = StringUtils.isBlank(startString);

        String filledStartString = fillEmpty(startString);
        String filledEndString = fillEmpty(endString);

        DateGroup startDateGroup = parseWithNatty(filledStartString);
        DateGroup endDateGroup = parseWithNatty(filledEndString);

        LocalDateTime startDateTime = convertDateGroupToLocalDateTime(startDateGroup);
        LocalDateTime endDateTime = convertDateGroupToLocalDateTime(endDateGroup);

        Set<String> startParseElements = isStartEmpty ?
                Collections.emptySet() :
                startDateGroup.getParseLocations().keySet();
        // If end datetime was empty to begin with, we have to pretend nothing was parsed.
        Set<String> endParseElements = isEndEmpty ? Collections.emptySet() : endDateGroup.getParseLocations().keySet();

        LocalDateTime[] adjustedDates = adjustDoubleDateToDefault(startDateTime, endDateTime, startParseElements,
                endParseElements);

        dateTimeResults.addAll(Arrays.asList(adjustedDates));

        return dateTimeResults;
    }

    public LocalDateTime parseDateTimeAsIs(String dateTimeString) {
        DateGroup parsedDateGroup = parseWithNatty(dateTimeString);

        return convertDateGroupToLocalDateTime(parsedDateGroup);
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
        LocalDateTime defaultDateTime = dateTime;
        defaultDateTime = fillDefaultDateTime(defaultDateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT.minusMinutes(1)),
                parseElements);

        defaultDateTime = adjustDateAfterReferenceByDays(defaultDateTime, LocalDateTime.now().withNano(0), parseElements);

        defaultDateTime = adjustAmPm(defaultDateTime, parseElements);

        return defaultDateTime;
    }

    private LocalDateTime[] adjustDoubleDateToDefault(LocalDateTime startDateTime, LocalDateTime endDateTime,
            Set<String> startParseElements, Set<String> endParseElements) {

        LocalDateTime adjustedStart;
        LocalDateTime adjustedEnd;
        LocalDateTime defaultDateTime = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0);

        //Fill in proper default dates
        adjustedStart = fillDefaultDateTime(startDateTime, defaultDateTime, startParseElements);
        adjustedEnd = fillDefaultDateTime(endDateTime, adjustedStart, endParseElements);
        adjustedEnd = adjustTimeAfterReferenceOneHour(adjustedEnd, adjustedStart, endParseElements);

        //To adjust the start time properly
        adjustedStart = fillDefaultDateTime(adjustedStart, adjustedEnd, startParseElements);
        adjustedStart = adjustTimeBeforeReferenceOneHour(adjustedStart, adjustedEnd, startParseElements);

        adjustedEnd = adjustTimeAfterReferenceOneHour(adjustedEnd, adjustedStart, endParseElements);
        adjustedEnd = adjustDateAfterReferenceByDays(adjustedEnd, adjustedStart, endParseElements);

        if (adjustedEnd.isBefore(LocalDateTime.now())) {
            // jointParse will detect if both dates can be adjusted
            Set<String> jointParse = new HashSet<>(startParseElements);
            jointParse.addAll(endParseElements);

            adjustedStart = adjustDateAfterReferenceByDays(adjustedStart, defaultDateTime,
                    jointParse);
            adjustedEnd = adjustDateAfterReferenceByDays(adjustedEnd, defaultDateTime,
                    jointParse);
        }

        adjustedStart = adjustAmPm(adjustedStart, startParseElements);
        adjustedEnd = adjustAmPm(adjustedEnd, endParseElements);

        return new LocalDateTime[] {adjustedStart, adjustedEnd};
    }

    private LocalDateTime fillDefaultDateTime(LocalDateTime dateTime, LocalDateTime defaultDateTime,
            Set<String> parseElements) {
        LocalDateTime adjustedDateTime = dateTime;

        if (!parseElements.contains(NATTY_TIME_FIELD)) {
            adjustedDateTime = LocalDateTime.of(adjustedDateTime.toLocalDate(), defaultDateTime.toLocalTime());
        }

        if (!parseElements.contains(NATTY_DATE_FIELD)) {
            adjustedDateTime = LocalDateTime.of(defaultDateTime.toLocalDate(), adjustedDateTime.toLocalTime());
        }

        return adjustedDateTime;
    }

    private LocalDateTime adjustAmPm(LocalDateTime dateTime, Set<String> parseElements) {

        LocalDateTime adjustedDateTime = dateTime.withNano(0);

        if (!parseElements.contains(NATTY_MERIDIAN_INDICATOR) && parseElements.contains(NATTY_TIME_FIELD)) {
            LocalTime time = dateTime.toLocalTime();

            if (time.isAfter(UNGODLY_HOUR_START) && time.isBefore(UNGODLY_HOUR_END)) {
                time = time.plusHours(12);
                adjustedDateTime = LocalDateTime.of(dateTime.toLocalDate(), time);
            }
        }

        return adjustedDateTime;
    }

    private LocalDateTime adjustDateAfterReferenceByDays(LocalDateTime dateTime, LocalDateTime reference,
            Set<String> parseElements) {
        LocalDateTime newDateTime = dateTime;
        if (!parseElements.contains(NATTY_DATE_FIELD)) {
            while (dateTime.isBefore(reference)) {
                newDateTime = newDateTime.plusDays(1);
            }
        }

        return newDateTime;
    }

    private LocalDateTime adjustTimeAfterReferenceOneHour(LocalDateTime dateTime, LocalDateTime reference,
            Set<String> parseElements) {
        if (!parseElements.contains(NATTY_TIME_FIELD)
                && (dateTime.isBefore(reference) || dateTime.isEqual(reference))) {

            if (parseElements.contains(NATTY_DATE_FIELD)) {
                dateTime = LocalDateTime.of(dateTime.toLocalDate(), reference.toLocalTime().plusHours(1));
            } else {
                dateTime = reference.plusHours(1);
            }

        }

        return dateTime;
    }

    private LocalDateTime adjustTimeBeforeReferenceOneHour(LocalDateTime dateTime, LocalDateTime reference,
            Set<String> parseElements) {
        LocalDateTime tempDateTime = dateTime;
        if (!parseElements.contains(NATTY_TIME_FIELD)
                && (tempDateTime.isAfter(reference) || tempDateTime.isEqual(reference))) {

            if (parseElements.contains(NATTY_DATE_FIELD)) {
                tempDateTime = LocalDateTime.of(tempDateTime.toLocalDate(), reference.toLocalTime().minusHours(1));
            } else {
                tempDateTime = reference.minusHours(1);
            }
        }

        return tempDateTime;
    }
}
