package com.helio.app.model;

import android.content.Context;
import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Collections.rotate(allDaysList, getLocalRotationDistance(firstDay));
        return allDaysList;
    }

    public static List<String> getShortDaysLocalOrder(String[] shortWeekdays, Day firstDay) {
        List<String> shortDayList = Arrays.asList(shortWeekdays);
        Collections.rotate(shortDayList, getLocalRotationDistance(firstDay));
        return shortDayList;
    }

    public static int getLocalRotationDistance(Day firstDay) {
        return -Arrays.asList(values()).indexOf(firstDay);
    }

    public static Day getFirstLocalDay(Context context) {
        WeekFields weekFields;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            weekFields = WeekFields.of(context.getResources().getConfiguration().getLocales().get(0));
        } else {
            weekFields = WeekFields.of(context.getResources().getConfiguration().locale);
        }

        return Day.values()[weekFields.getFirstDayOfWeek().getValue() - 1];
    }
}
