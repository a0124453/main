package lifetracker.parser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

public class DateTimeParser {
    private static final String DATE_PATTERN0 = "[a-zA-Z]+|[a-zA-Z]+ [a-zA-Z]+";
    private static final String DATE_PATTERN1 = "\\d+ (?:[a-zA-Z]+&&^(?:am|AM|pm|PM))";
    private static final String DATE_PATTERN2 = "\\d+ [a-zA-Z]+ \\d+";
    private static final String DATE_PATTERN3 = "\\d{2,}+";
    private static final String DATE_PATTERN4 = "\\d+(?:/|-)\\d+";
    private static final String DATE_PATTERN5 = "\\d+(?:/|-)\\d+(?:/|-)\\d{2,}+";
    private static final String TIME_PATTERN = "\\d+(?::\\d+)?\\s?(?:am|AM|pm|PM)?";
    private static final String DATE_TIME_PATTERN = String.format("^(%s|%s|%s|%s|%s|%s)?\\s?(%s)?$", 
            DATE_PATTERN0, DATE_PATTERN1, DATE_PATTERN2, 
            DATE_PATTERN3, DATE_PATTERN4, DATE_PATTERN5, TIME_PATTERN);
        
    boolean isDateTime(String dateTimeString) {
        boolean isValidDAteTime;
        
        if (dateTimeString.matches(DATE_TIME_PATTERN)) {
            isValidDAteTime = true;
        } else {
            isValidDAteTime = false;
        }
        return isValidDAteTime;
    }
    LocalDateTime parse(String endDateTimeString) {
        LocalDateTime endDateTime;
        
        String endDateString;
        String endTimeString;
        LocalDate endDate;
        LocalTime endTime;
        
        return endDateTime;
    }
    
    //start date before end date
    LocalDateTime[] parse(String startDate, String endDate) {
        return null;
    }
    

}
