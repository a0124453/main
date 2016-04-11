package lifetracker.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

//@@author A0091173J

/**
 * This class helps GSON identify the correct implementation of {@code CalendarEntry} to deserialize into.
 */
public class CalendarEntryImplDeserializer implements JsonDeserializer<CalendarEntry> {

    private static final String SERIAL_CLASS_ID_FIELD = "SERIAL_TYPE_IDENTIFIER";

    private static final Map<String, Class<? extends CalendarEntry>> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put("DeadlineTask", DeadlineTask.class);
        CLASS_MAP.put("Event", Event.class);
        CLASS_MAP.put("GenericEntry", GenericEntry.class);
        CLASS_MAP.put("RecurringEvent", RecurringEvent.class);
        CLASS_MAP.put("RecurringTask", RecurringTask.class);
    }

    @Override
    public CalendarEntry deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Class<? extends CalendarEntry> entryClass = resolveEntryClass(jsonElement);

        Gson gsonParser = new GsonBuilder().create();

        return gsonParser.fromJson(jsonElement, entryClass);
    }

    private Class<? extends CalendarEntry> resolveEntryClass(JsonElement jsonElement) {

        JsonElement classSerialID = jsonElement.getAsJsonObject().get(SERIAL_CLASS_ID_FIELD);

        if (classSerialID == null) {
            throw new JsonParseException("Invalid file format! Save file might have been corrupted!");
        }

        return CLASS_MAP.get(classSerialID.getAsString());
    }
}
