
public class CalendarUnused {
    void filterByStartDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate startDate) {
        if (startDate == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getStart() == null) {
                iterator.remove();
            } else {
                LocalDate entryStartDate = entry.getValue().getStart().toLocalDate();
                if (!entryStartDate.equals(startDate)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByStartTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime startTime) {
        if (startTime == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getStart() == null) {
                iterator.remove();
            } else {
                LocalTime entryStartTime = entry.getValue().getStart().toLocalTime();
                if (!entryStartTime.equals(startTime)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByEndDate(TreeMap<Integer, CalendarEntry> treeMap, LocalDate endDate) {
        if (endDate == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getEnd() == null) {
                iterator.remove();
            } else {
                LocalDate entryEndDate = entry.getValue().getEnd().toLocalDate();
                if (!entryEndDate.equals(endDate)) {
                    iterator.remove();
                }
            }
        }
    }

    void filterByEndTime(TreeMap<Integer, CalendarEntry> treeMap, LocalTime endTime) {
        if (endTime == null) {
            return;
        }
        Iterator<Map.Entry<Integer, CalendarEntry>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CalendarEntry> entry = iterator.next();
            if (entry.getValue().getEnd() == null) {
                iterator.remove();
            } else {
                LocalTime entryEndTime = entry.getValue().getEnd().toLocalTime();
                if (!entryEndTime.equals(endTime)) {
                    iterator.remove();
                }
            }
        }
    }

}
