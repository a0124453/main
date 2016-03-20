package lifetracker.parser;

import com.joestelmach.natty.DateGroup;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shen Yichen <2007.yichen@gmail.com>
 */
class DateTimeParser {
    private static DateTimeParser instance = new DateTimeParser();

    static DateTimeParser getInstance() {
        return instance;
    }

    private static final String EMPTY_DATE_DEFAULT_STRING = "today";

    private final com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();

    private DateTimeParser() {
    }

    boolean isDateTime(String dateTimeString) {
        return nattyParser.parse(dateTimeString).size() == 1;
    }

    LocalDateTime parseSingleDateTime(String dateTimeString) {
        assert isDateTime(dateTimeString);

        LocalDateTime parsedDateTimeObj = parseWithNatty(dateTimeString);

        return parsedDateTimeObj;
    }

    List<LocalDateTime> parseDoubleDateTime(String startString, String endString) {
        List<LocalDateTime> dateTimeResults = new ArrayList<>(2);

        startString = fillEmpty(startString);
        endString = fillEmpty(endString);

        LocalDateTime startDateTime = parseWithNatty(startString);
        LocalDateTime endDateTime = parseWithNatty(endString);

        dateTimeResults.add(startDateTime);
        dateTimeResults.add(endDateTime);

        return dateTimeResults;
    }

    private String fillEmpty(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return "today";
        } else {
            return dateTimeString;
        }
    }

    private LocalDateTime parseWithNatty(String dateTimeString) {
        DateGroup parsedDateGroup = nattyParser.parse(dateTimeString).get(0);
        Date parsedDate = parsedDateGroup.getDates().get(0);

        return convertDateToLocalDateTime(parsedDate);
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
