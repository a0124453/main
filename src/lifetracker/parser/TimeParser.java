package lifetracker.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class TimeParser {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "DD-MM-YY");
    
    private TemporalAccessor parse(String v) {
        return formatter.parseBest(v,
                                   LocalTime::from,
                                   LocalDateTime::from,
                                   LocalDate::from);
    }
    
    public static void main(String[] args) {
        TemporalAccessor dateTime1 = formatter.parse("05-06-94");
        System.out.println(dateTime1);
    }

}
