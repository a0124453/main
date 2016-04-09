
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

//@@author A0114240B-unused
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
