package com.helio.app

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestSensors {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        // otherwise the elements won't load
        Utils.setHubIP("10.0.2.2")
        // navigate to the desired fragment
        onView(withId(R.id.navigation_sensors))
                .perform(ViewActions.click())
    }

    @Test
    // check that we can add a new motion sensor using the "+"-button
    fun registerNewMotionSensor() {
        val startCount = Utils.getCountFromRecyclerView(R.id.sensorsRCView)
        onView(withId(R.id.add_motion_button))
                .perform(ViewActions.click())
        // exit from new motion sensor's settings
        Espresso.pressBack()
        onView(withId(R.id.sensorsRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    @Test
    // check that we can add a new light sensor using the "+"-button
    fun registerNewLightSensor() {
        val startCount = Utils.getCountFromRecyclerView(R.id.sensorsRCView)
        onView(withId(R.id.add_light_button))
                .perform(ViewActions.click())
        // exit from new light sensor's settings
        Espresso.pressBack()
        onView(withId(R.id.sensorsRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    @Test
    // check that we can toggle the switch for sensor #1
    fun toggleSwitch0() {
        onView(withId(R.id.sensorsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, Utils.clickOnViewChild(R.id.activate_switch)))
    }

    @Test
    // check that we can navigate into a single sensor settings fragment
    fun enterSensor0Settings() {
        onView(withId(R.id.sensorsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, ViewActions.click()))
        // check that the IP address field is displayed to confirm we're in settings fragment
        onView(withId(R.id.ip_address)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    // check that we can update a sensor's active blinds
    fun adjustSensor0ActiveBlinds() {
        // enter the settings fragment for this blind
        onView(withId(R.id.sensorsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, ViewActions.click()))
        // toggle the first blind for this sensor
        onView(withId(R.id.blindsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, Utils.clickOnViewChild(R.id.checkbox)))
        // toggle the second blind for this sensor
        onView(withId(R.id.blindsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (1, Utils.clickOnViewChild(R.id.checkbox)))
    }
}
