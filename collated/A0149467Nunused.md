# A0149467Nunused
###### /unused/LogicCalendarStub.java
``` java

public class LogicCalendarStub implements CalendarList {

    private List<CalendarEntry> taskList = new ArrayList<>();
    private List<CalendarEntry> eventList = new ArrayList<>();

    @Override
    public List<CalendarEntry> getTaskList() {
        return taskList;
    }

    @Override
    public List<CalendarEntry> getEventList() {
        return eventList;
    }

    @Override
    public int add(String name) {
        taskList.add(new CalendarEntryStub(name, null, null));
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime due) {
        taskList.add(new CalendarEntryStub(name, null, due));
        return 0;
    }

    @Override
    public int add(String name, LocalDateTime start, LocalDateTime end) {
        eventList.add(new CalendarEntryStub(name, start, end));
        return 0;
    }

    @Override
    public CalendarEntry delete(int id) {

        return null;
    }

    @Override
    public CalendarEntry update(int id, String newName, LocalDateTime newStart, LocalDateTime newEnd) {

        return null;
    }

    @Override
    public List<CalendarEntry> list(String toSearch) {

        return null;
    }

    class CalendarEntryStub implements CalendarEntry {

        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int id;

        public CalendarEntryStub(String name, LocalDateTime startTime, LocalDateTime endTime) {
            this.name = name;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {

        }

        @Override
        public LocalDateTime getStart() {
            return startTime;
        }

        @Override
        public void setStart(LocalDateTime start) {

        }

        @Override
        public LocalDateTime getEnd() {
            return endTime;
        }

        @Override
        public void setEnd(LocalDateTime end) {

        }

        @Override
        public LocalTime getStartTime() {
            return startTime == null ? null : startTime.toLocalTime();
        }

        @Override
        public LocalTime getEndTime() {
            return endTime == null ? null : endTime.toLocalTime();
        }
        
        @Override
        public EntryType getType() {
            return null;
        }

        @Override
        public boolean isToday() {
            return false;
        }

        @Override
        public boolean isOngoing() {
            return false;
        }

        @Override
        public boolean isOver() {
            return false;
        }

        @Override
        public boolean equals(CalendarEntry entry) {
            return equals((Object) entry);
        }
        
        @Override
        public CalendarEntry copy() {
            return null;
        }
    }
}
```
###### /unused/LogicCommandStub.java
``` java

public class LogicCommandStub implements CommandObject {
    
    private static final String MESSAGE_ADDED = "\"%1$s\" is added.";
    
    private final String name;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    
    private String comment = MESSAGE_ERROR;
    
    public LogicCommandStub(String name) {
        this.name = name;
        this.startDateTime = null;
        this.endDateTime = null;
    }
    
    public LogicCommandStub(String name, LocalDateTime dueDateTime) {
        this.name = name;
        this.startDateTime = null;
        this.endDateTime = dueDateTime;
    }
    
    public LogicCommandStub(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    
    @Override
    public CalendarList execute(CalendarList calendar){
        assert calendar != null;

        if (endDateTime == null) {
            calendar.add(name);
        } else if (startDateTime == null) {
            calendar.add(name, endDateTime);
        } else {
            calendar.add(name, startDateTime, endDateTime);
        }

        comment = String.format(MESSAGE_ADDED, name);

        return new CalendarListImpl();
    }
    
    @Override
    public CalendarList undo(CalendarList calendar){
        return null;
    }
    
    @Override
    public String getComment(){
        return this.comment;
    }
}
```
###### /unused/LogicParserStub.java
``` java

public class LogicParserStub implements Parser {
    
    @Override
    public CommandObject parse(String userInput) {
        
        int position = userInput.indexOf(" ");
        String item = userInput.substring(position + 1);
        
        if(userInput.contains("by")) {
            String[] content = item.split(" by ");
            String name = content[0];
            
            try {
                LocalDateTime deadline = LocalDateTime.parse(content[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return new LogicCommandStub(name, deadline);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException();
            }
        }
        
        else if(userInput.contains("from")) {
            String[] content = item.split(" from ");
            String name = content[0];
            
            try {
                String[] startEnd = content[1].split(" to ");
                LocalDateTime start = LocalDateTime.parse(startEnd[0],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime end = LocalDateTime.parse(startEnd[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return new LogicCommandStub(name, start, end);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException();
            }
        }
        
        else {
            String name = item;
            return new LogicCommandStub(name);
        }
    }
}
```
###### /unused/LogicStorageStub.java
``` java

public class LogicStorageStub implements Storage {

    @Override
    public void setStore(String destination) throws IOException {
        
    }
    
    @Override
    public void store(CalendarList calendar) throws IOException {
        
    }
    
    @Override
    public CalendarList load(CalendarList calendar) throws IOException {
        return calendar;
    }
    
    @Override
    public void close() throws Exception {
        
    }
}
```
