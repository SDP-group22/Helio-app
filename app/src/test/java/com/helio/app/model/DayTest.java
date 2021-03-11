package com.helio.app.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayTest {

    public static final String[] SHORT_WEEKDAYS = {"M", "T", "W", "T", "F", "S", "S"};

    @Test
    void getEnumFromNameCorrectNameWorks() {
        Day day = Day.getEnumFromName("Monday");
        assertEquals(Day.MONDAY, day);
    }

    @Test
    void getValuesLocalOrderMondayFirst() {
        assertEquals(Day.MONDAY, Day.getValuesLocalOrder(Day.MONDAY).get(0));
    }

    @Test
    void getValuesLocalOrderTuesdayFirst() {
        assertEquals(Day.TUESDAY, Day.getValuesLocalOrder(Day.TUESDAY).get(0));
    }

    @Test
    void getValuesLocalOrderSundayFirst() {
        assertEquals(Day.SATURDAY, Day.getValuesLocalOrder(Day.SUNDAY).get(6));
    }

    @Test
    void getShortDaysLocalOrderMondayFirst() {
        assertEquals("M", Day.getShortDaysLocalOrder(SHORT_WEEKDAYS.clone(), Day.MONDAY).get(0));
    }

    @Test
    void getShortDaysLocalOrderTuesdayFirst() {
        assertEquals("T", Day.getShortDaysLocalOrder(SHORT_WEEKDAYS.clone(), Day.TUESDAY).get(0));
    }

    @Test
    void getShortDaysLocalOrderSundayFirst() {
        assertEquals("S", Day.getShortDaysLocalOrder(SHORT_WEEKDAYS.clone(), Day.SUNDAY).get(6));
    }
}