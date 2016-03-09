package lifetracker.parser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.LocalDate;

public class DateTimeParser {
    private static final String END_TIME_DEFAULT = "2359";
    private static final String START_TIME_DEFAULT = "0900";
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
        
    static boolean isDateTime(String dateTimeString) {
        boolean isValidDAteTime;
        
        if (dateTimeString.matches(DATE_TIME_PATTERN)) {
            isValidDAteTime = true;
        } else {
            isValidDAteTime = false;
        }
        
        return isValidDAteTime;
    }
    
    static LocalDateTime parse(String endDateTimeString) {

        String endDateString = null;
        String endTimeString = null;
        
        Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
        Matcher matcher = pattern.matcher(endDateTimeString);
        
        matcher.find();
        endDateString = matcher.group(1);
        endTimeString = matcher.group(2);
        
        
        if (endDateString == null) {
            endDateString = "today";
        }

        if (endTimeString == null) {
            endTimeString = END_TIME_DEFAULT;
        }
        
        LocalDate endDate = DateParser.parse(endDateString);
        LocalTime endTime = TimeParser.parse(endTimeString);
        
        LocalDateTime endDateTime = endDate.atTime(endTime);
        
        if (endDateTime.isBefore(LocalDateTime.now())) {
            endDateTime = endDateTime.plusDays(1);
        }
        
        return endDateTime;
    }
    
    //start date before end date
    static LocalDateTime[] parse(String startDateTimeString, String endDateTimeString) {
        LocalDateTime[] dates = new LocalDateTime[2];
        String startDateString = null;
        String startTimeString = null;
        String endDateString = null;
        String endTimeString = null;
        
        Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
        
        Matcher matcher = pattern.matcher(startDateTimeString);
        matcher.find();
        startDateString = matcher.group(1);
        startTimeString = matcher.group(2);
        
        matcher = pattern.matcher(endDateTimeString);
        matcher.find();
        endDateString = matcher.group(1);
        endTimeString = matcher.group(2);
        
        if (startDateString == null) {
            startDateString = processNullStartDate(endDateString);
        }
        
        if (startDateString.equals("now")) {
            startTimeString = getCurrentTime();
        }
        
        if (endDateString == null) {
            endDateString = startDateString;
        }
        
        if (startTimeString == null) {
            startTimeString = processNullStartTime(startDateString);
        }
        
        if (endTimeString == null) {
            endTimeString = processNullEndTime(startDateString, endDateString, startTimeString);
         }
        
        LocalDate startDate = DateParser.parse(startDateString);
        LocalTime startTime = TimeParser.parse(startTimeString);
        LocalDateTime startDateTime = startDate.atTime(startTime);
        
        LocalDate endDate = DateParser.parse(endDateString);
        LocalTime endTime = TimeParser.parse(endTimeString);
        LocalDateTime endDateTime = endDate.atTime(endTime);

        dates[0] = startDateTime;
        dates[1] = endDateTime;  
        
        return dates;
    }
    
    private static String processNullEndTime(String startDateString, String endDateString, String startTimeString) {
        String endTimeString;
        
        if (startDateString.equals(endDateString)) {
            endTimeString = addOneHourTo(startTimeString);
        } else {
            endTimeString = END_TIME_DEFAULT;
        }
        
        return endTimeString;
        
    }

    private static String addOneHourTo(String startTimeString) {
        LocalTime startTime = TimeParser.parse(startTimeString);
        startTime = startTime.plusHours(1);
        
        String hour = Integer.toString(startTime.getHour());
        String minute = Integer.toString(startTime.getMinute());
        String endTimeString = new String(hour + minute);
       
        return endTimeString;
    }

    private static String processNullStartTime(String startDateString) {
        String startTimeString;
        if (startDateString.equals("today")) {
            startTimeString = getCurrentTime();
        } else {
            startTimeString = START_TIME_DEFAULT;
        }
        return startTimeString;
    }

    private static String getCurrentTime() {
        String hour = Integer.toString(LocalTime.now().getHour());
        String minute = Integer.toString(LocalTime.now().getMinute());
        String currentTimeString = new String(hour + minute);
        return currentTimeString;
    }

    private static String processNullStartDate (String endDateString) {
        String startDateString;
        
        if (endDateString == null) {
            startDateString = new String("today");
        } else {
            startDateString = endDateString;
        }
        
        return startDateString;
        
    }
    
    
    
    

}
