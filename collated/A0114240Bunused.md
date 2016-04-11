# A0114240Bunused
###### /unused/DateParser.java
``` java
/*
 * This class was previously used to parse a set of strings identified by 
 * different regular expression patterns into a LocalDate object.
 * This class was replaced by external library called Natty.
 */
public class DateParser {
    static final String PATTERN_0_REGEX = "^([a-zA-Z]+)|([a-zA-Z]+ [a-zA-Z]+)$";
    static final int PATTERN_0 = 0;
    static final String PATTERN_1_REGEX = "^(\\d+) ([a-zA-Z]+)$";
    static final int PATTERN_1 = 1;
    static final String PATTERN_2_REGEX = "^(\\d+) ([a-zA-Z]+) (\\d+)$";
    static final int PATTERN_2 = 2;
    static final String PATTERN_3_REGEX = "^(\\d{2,}+)$";
    static final int PATTERN_3 = 3;
    static final String PATTERN_4_REGEX = "(\\d+)(?:/|-)(\\d+)";
    static final int PATTERN_4 = 4;
    static final String PATTERN_5_REGEX = "(\\d+)(?:/|-)(\\d+)(?:/|-)(\\d{2,}+)";
    static final int PATTERN_5 = 5;
    
    static ArrayList<String> possibleDatePatterns = new ArrayList<String>();
    
    static final String[] KEYWORDS = {"today", "now", "tomorrow", "tmrw", "tmr",
            "monday", "mon", "m", "tuesday", "tues", "t", "wednesday", "wed", "w",
            "thursday", "thur", "th", "friday", "fri", "f", "saturday", "sat", "s",
            "sunday", "sun", "su"};
    
    public static boolean isValidDay(String input) {
        boolean isValidDayString = false;
        if (input.matches(PATTERN_0_REGEX)) {
            if (input.startsWith("next ")) {
                input = input.replace("next ", "");
            }
            isValidDayString = isContainKeywords(input);
        }

        return isValidDayString;
    }

    private static boolean isContainKeywords(String input) {
        boolean isContainKeyword = false;

        for (String keyword : KEYWORDS) {
            if (input.equals(keyword)) {
                isContainKeyword = true;
                break;
            }
        }
        return isContainKeyword;
    }

    public static LocalDate parse(String inputDate) {
        LocalDate date = null;
        createPossibleDatePatterns();
        int pattern = matchDateToPatterns(inputDate);

        switch (pattern) {
            case PATTERN_0 :
                date = processDay(inputDate);
                break;
            case PATTERN_1 :
            case PATTERN_2 :
                date = processHumanDate(inputDate);
                break;
            case PATTERN_3 :
                date = processDateWithoutFormat(inputDate);
                break;
            case PATTERN_4 :
            case PATTERN_5 :
                date = processDateWithFormat(inputDate);
                break;
            default :
                break;
        }
        
        return date;
    }

    private static int matchDateToPatterns(String inputDate) {
        int pattern = -1;
        
        for (String possiblePattern : possibleDatePatterns) {
            if (inputDate.matches(possiblePattern)) {
                pattern = possibleDatePatterns.indexOf(possiblePattern);
                break;
            }
        }
        return pattern;
    }

    private static void createPossibleDatePatterns() {
        possibleDatePatterns.clear();
        possibleDatePatterns.add(PATTERN_0_REGEX);
        possibleDatePatterns.add(PATTERN_1_REGEX);
        possibleDatePatterns.add(PATTERN_2_REGEX);
        possibleDatePatterns.add(PATTERN_3_REGEX);
        possibleDatePatterns.add(PATTERN_4_REGEX);
        possibleDatePatterns.add(PATTERN_5_REGEX);
    }

    private static LocalDate processDateWithFormat(String inputDate) {
        LocalDate date = null;
        String[] inputParam = inputDate.split("/|\\s|-");
        
        if (inputParam.length == 2) {
            int thisYear = LocalDate.now().getYear();
            inputDate = inputDate.concat("/" + thisYear);
        }
                
        ArrayList<String> possibleDateFormats = new ArrayList<String>();
        ArrayList<DateTimeFormatter> possibleDateFormatters = new ArrayList<DateTimeFormatter>();
        
        possibleDateFormats.add("d/M/yy");
        possibleDateFormats.add("d-M-yy");
        possibleDateFormats.add("d-M/yy");
        
        for (String format:possibleDateFormats) {
            possibleDateFormatters.add(createFormatter(format));
        }
        
        
        int i = 0;
        while (i < possibleDateFormatters.size()) {
            try {
                date = LocalDate.parse(inputDate, possibleDateFormatters.get(i));
                break;
            } catch (Exception e) {
                i ++;
            }
        }
        
        return date;
    }

    private static DateTimeFormatter createFormatter(String format) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .appendPattern(format)
                .toFormatter();
        return formatter;
    }

    private static LocalDate processDateWithoutFormat(String inputDate) {
        LocalDate date = null;
        if (inputDate.length() == 2) {
            int thisMonthValue = LocalDate.now().getMonthValue();
            String thisMonth;
            
            if (thisMonthValue < 10) {
                thisMonth = "0" + thisMonthValue;
            } else {
                thisMonth = Integer.toString(thisMonthValue);
            }
            inputDate = inputDate.concat(thisMonth);
            
        }
        
        if (inputDate.length() <= 4) {
            String thisYear = Integer.toString(LocalDate.now().getYear());           
            inputDate = inputDate.concat(thisYear);
        }
        
        
        try {
            date = LocalDate.parse(inputDate, createFormatter("ddMMyy"));
        } catch (DateTimeParseException e) {
            
        }
        
        
        return date;
    }

    private static LocalDate processHumanDate(String inputDate) {
        String[] inputParam = inputDate.split(" ");
        
        if (inputParam.length == 2) {
            int thisYear = LocalDate.now().getYear();
            inputDate = inputDate.concat(" " + thisYear);
        }
        
        LocalDate date = null;  
        
        try {
            date = LocalDate.parse(inputDate, createFormatter("dd MMM yy"));
        } catch (DateTimeParseException e) {
            
        }
        
        return date;
    }

    private static LocalDate processDay(String inputDate) {
        LocalDate date = null;
        
        inputDate = inputDate.trim().toLowerCase();
        inputDate = inputDate.replace("next ", "");
        
        switch (inputDate) {
            case "today":
            case "now":
                date = LocalDate.now();
                break;
            case "tomorrow":
            case "tmrw":
            case "tmr":
                date = LocalDate.now().plusDays(1);
                break;
            case "monday":
            case "mon":
            case "m":
                date = processSpecificDay(DayOfWeek.MONDAY);
                break;
            case "tuesday":
            case "tue":
            case "t":
                date = processSpecificDay(DayOfWeek.TUESDAY);
                break;
            case "wednesday":
            case "wed":
            case "w":
                date = processSpecificDay(DayOfWeek.WEDNESDAY);
                break;
            case "thursday":
            case "thur":
            case "thu":
            case "th":
                date = processSpecificDay(DayOfWeek.THURSDAY);
                break;
            case "friday":
            case "fri":
            case "f":
                date = processSpecificDay(DayOfWeek.THURSDAY);
                break; 
            case "saturday":
            case "sat":
            case "s":
                date = processSpecificDay(DayOfWeek.SATURDAY);
                break;
            case "sunday":
            case "sun":
            case "su":
                date = processSpecificDay(DayOfWeek.SUNDAY);
                break;
            default:
                date = LocalDate.now();
                break;
        }
        
        return date;
    }

    private static LocalDate processSpecificDay(DayOfWeek day) {
        LocalDate date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day));
        return date;
    }

}
```
###### /unused/DateTimeParser.java
``` java
/*
* This class was previously used to parse a set of strings identified by 
* different regular expression patterns into a LocalDateTime object.
* This class was replaced by external library called Natty.
*/
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
        boolean isValidDateTime;
        
        if (dateTimeString.matches(DATE_TIME_PATTERN)) {
            if (dateTimeString.matches(DATE_PATTERN0)) {
                isValidDateTime = DateParser.isValidDay(dateTimeString);
            } else {
                isValidDateTime = true;
            }
        } else {
            isValidDateTime = false;
        }
        
        return isValidDateTime;
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
        
        if (endDateTime.isBefore(startDateTime)) {
            endDateTime.plusDays(1);
        }

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
        String endTimeString;
        LocalTime startTime = TimeParser.parse(startTimeString);
        startTime = startTime.plusHours(1);
        
        String hour = Integer.toString(startTime.getHour());
        Integer minute = startTime.getMinute();
        
        if (minute < 10) {
            endTimeString = new String(hour + "0" + minute);
        } else {
            endTimeString = new String(hour + minute);
        }
       
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
```
###### /unused/DateTimeParserTest.java
``` java
/*
* This class was previously used to test DateTimeParser.
* DateTimeParser was replaced by external library called Natty.
*/
public class DateTimeParserTest {

    private LocalDateTime[] dates;

    @Test
    public void dateTimeParserTest() {
        dates = new LocalDateTime[2];
        dates[0] = LocalDateTime.of(2016, 3, 11, 16, 0);
        dates[1] = LocalDateTime.of(2016, 3, 11, 17, 0);
        assertArrayEquals(dates, DateTimeParser.parse("today 4pm", "today"));
        
        
        dates[0] = LocalDateTime.of(2016, 3, 11, 16, 0);
        dates[1] = LocalDateTime.of(2016, 3, 11, 15, 0);
        assertArrayEquals(dates, DateTimeParser.parse("today 4pm", "3pm"));
        
    }

}
```
###### /unused/TaskObject.java
``` java
//Replaced by CommandObject
public class TaskObject {
    private String command;
    private String task;


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
    
}
```
###### /unused/TimeParser.java
``` java
/*
* This class was previously used to parse a set of strings identified by 
* different regular expression patterns into a LocalTime object.
* This class was replaced by external library called Natty.
*/
public class TimeParser {
    static ArrayList<String> possibleTimeFormats = new ArrayList<String>();
    static ArrayList<DateTimeFormatter> possibleTimeFormatters = new ArrayList<DateTimeFormatter>();

    public static LocalTime parse(String timeInput) {
        LocalTime time = null;
        createPossibleTimeFormats();
        createPossibleTimeFormatters();
        int i = 0;
        while (i < possibleTimeFormatters.size()) {
            try {
                time = LocalTime.parse(timeInput, possibleTimeFormatters.get(i));
                break;
            } catch (DateTimeParseException e) {
                i++;
            }
        }
        
        return time;
    }

    private static void createPossibleTimeFormatters() {
        for (String format : possibleTimeFormats) {
            possibleTimeFormatters.add(createFormatter(format));
        }
    }

    private static void createPossibleTimeFormats() {
        possibleTimeFormats.add("hh[' ']a");
        possibleTimeFormats.add("KK[' ']a");
        possibleTimeFormats.add("hhmm[' ']a");
        possibleTimeFormats.add("hh:mm[' ']a");
        possibleTimeFormats.add("KKmm[' ']a");
        possibleTimeFormats.add("KK:mm[' ']a");
        possibleTimeFormats.add("HH");
        possibleTimeFormats.add("kk");
        possibleTimeFormats.add("HHmm");
        possibleTimeFormats.add("HH:mm");
        possibleTimeFormats.add("kkmm");
        possibleTimeFormats.add("kk:mm");
    }

    private static DateTimeFormatter createFormatter(String format) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
                .appendPattern(format).toFormatter();
        return formatter;
    }

}
```
###### /unused/TimeParserTest.java
``` java
/*
* This class was previously used to test TimeParser.
* TimeParser was replaced by external library called Natty.
*/
public class TimeParserTest {

    @Test
    public void timeParserTest() {
        assertEquals(LocalTime.of(11, 0), TimeParser.parse("1100"));
        assertEquals(LocalTime.of(11, 0), TimeParser.parse("1100am"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("1100pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("1100 pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11:00pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11:00 pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11pm"));
        assertEquals(LocalTime.of(23, 0), TimeParser.parse("11 pm"));
    }

}
```
###### /unused/ItemUI.java
``` java
//This class was replaced by LogicEvent and LogicTask.
public class ItemUI {

   SimpleListProperty<String> item;

    public List<String> getItem() {
        return item.get();
    }

    public ItemUI(List<String> item) {
        super();
        ObservableList<String> observableList = FXCollections.observableArrayList(item);
        this.item = new SimpleListProperty<String>((ObservableList<String>) observableList);
    }
}
```
