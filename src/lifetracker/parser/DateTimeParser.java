package lifetracker.parser;

import java.time.LocalDateTime;

public interface DateTimeParser {
        
    boolean isDateTime(String dateTimeString);
    LocalDateTime parse(String endDate);
    
    //start date before end date
    LocalDateTime[] parse(String startDate, String endDate);
    

}
