package lifetracker.parser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

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
