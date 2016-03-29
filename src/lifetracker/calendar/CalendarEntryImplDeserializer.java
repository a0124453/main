package lifetracker.calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

public class CalendarEntryImplDeserializer implements JsonDeserializer<CalendarEntry> {

    @Override
    public CalendarEntry deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Gson typeQuery = new Gson();

        TypeAdapter temporalAmountTypeAdapter;

        if (isPeriodRecurring(jsonElement)) {

            temporalAmountTypeAdapter = typeQuery.getAdapter(Period.class);
        } else {
            temporalAmountTypeAdapter = typeQuery.getAdapter(Duration.class);
        }
        Gson gsonParser = new GsonBuilder()
                .registerTypeHierarchyAdapter(TemporalAmount.class, temporalAmountTypeAdapter).create();

        return gsonParser.fromJson(jsonElement, CalendarEntryImpl.class);
    }

    private boolean isPeriodRecurring(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("period").getAsJsonObject().has("years");
    }
}
