package com.helio.app.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.helio.app.model.Day;
import com.helio.app.model.Schedule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleDeserializerTest {
    public static final String SCHEDULE_ONE_DAY = "{\n" +
            "      \"active\": false,\n" +
            "      \"days\": [\n" +
            "        \"Saturday\"\n" +
            "      ],\n" +
            "      \"gradient\": 7216,\n" +
            "      \"id\": 7,\n" +
            "      \"motor_ids\": [\n" +
            "        999\n" +
            "      ],\n" +
            "      \"name\": \"newName\",\n" +
            "      \"target_level\": 1,\n" +
            "      \"time\": \"a\"\n" +
            "    }";
    public static final String SCHEDULE_TWO_DAYS = "{\n" +
            "      \"active\": false,\n" +
            "      \"days\": [\n" +
            "        \"Saturday\",\n" +
            "        \"Sunday\"\n" +
            "      ],\n" +
            "      \"gradient\": 7216,\n" +
            "      \"id\": 7,\n" +
            "      \"motor_ids\": [\n" +
            "        999\n" +
            "      ],\n" +
            "      \"name\": \"newName\",\n" +
            "      \"target_level\": 1,\n" +
            "      \"time\": \"a\"\n" +
            "    }";
    private static Gson gson;

    @BeforeAll
    static void beforeAll() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializer
        gsonBuilder.registerTypeAdapter(Schedule.class, new ScheduleDeserializer());
        gson = gsonBuilder.create();
    }

    @Test
    void scheduleDeserializerSingleDayCorrect() {
        Schedule schedule = gson.fromJson(SCHEDULE_ONE_DAY, Schedule.class);
        assertEquals(1, schedule.getDays().size());
        assertEquals(Day.SATURDAY, schedule.getDays().get(0));
        assertEquals(1, schedule.getTargetLevel());
    }

    @Test
    void scheduleDeserializerMultipleDaysCorrect() {
        Schedule schedule = gson.fromJson(SCHEDULE_TWO_DAYS, Schedule.class);
        assertEquals(2, schedule.getDays().size());
        assertEquals(Day.SATURDAY, schedule.getDays().get(0));
        assertEquals(Day.SUNDAY, schedule.getDays().get(1));
    }

}