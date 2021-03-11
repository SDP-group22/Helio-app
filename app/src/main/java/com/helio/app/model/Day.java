package com.helio.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the day of the week. DayOfWeek already exists in Java, but only works in Android API level 26+
 */
public enum Day {

    @SerializedName("Monday")
    MONDAY("Monday"),
    @SerializedName("Tuesday")
    TUESDAY("Tuesday"),
    @SerializedName("Wednesday")
    WEDNESDAY("Wednesday"),
    @SerializedName("Thursday")
    THURSDAY("Thursday"),
    @SerializedName("Friday")
    FRIDAY("Friday"),
    @SerializedName("Saturday")
    SATURDAY("Saturday"),
    @SerializedName("Sunday")
    SUNDAY("Sunday");

    // Lookup map for getting a day from a String from the API
    private static final Map<String, Day> lookup = new HashMap<>();

    static {
        for (Day d : Day.values()) {
            lookup.put(d.dayName, d);
        }
    }

    public final String dayName;

    Day(String dayName) {
        this.dayName = dayName;
    }

    /**
     * Gets a day enum from its string representation
     */
    public static Day getEnumFromName(String name) throws IllegalArgumentException {
        return lookup.get(name);
    }

    public static List<Day> getValuesLocalOrder(Day firstDay) {
        List<Day> allDaysList = Arrays.asList(values());
        Collections.rotate(allDaysList, -allDaysList.indexOf(firstDay));
        return allDaysList;
    }

    public static List<String> getShortDaysLocalOrder(String[] shortWeekdays, Day firstDay) {
        List<Day> allDaysList = Arrays.asList(values());
        List<String> shortDayList = Arrays.asList(shortWeekdays);
        Collections.rotate(shortDayList, -allDaysList.indexOf(firstDay));
        return shortDayList;
    }
}
