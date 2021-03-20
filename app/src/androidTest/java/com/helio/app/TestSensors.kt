package com.helio.app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest

import org.junit.Before

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestSensors {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
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
        onView(withId(R.id.sensorsRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    @Test
    // check that we can add a new light sensor using the "+"-button
    fun registerNewLightSensor() {
        val startCount = Utils.getCountFromRecyclerView(R.id.sensorsRCView)
        onView(withId(R.id.add_light_button))
                .perform(ViewActions.click())
        onView(withId(R.id.sensorsRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    @Test
    // check that we can toggle the switch for sensor #1
    fun toggleSwitch0() {
        onView(withId(R.id.sensorsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, clickOnViewChild(R.id.activate_switch)))
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
    // check that we can update a schedule's activate days
    fun adjustSchedule0ActiveDays() {
        // enter the settings fragment for this schedule
        onView(withId(R.id.schedulesRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, ViewActions.click()))
        // update this schedule's activate days
        onView(withId(R.id.day1))
                .perform(ViewActions.click())
        onView(withId(R.id.day3))
                .perform(ViewActions.click())
        onView(withId(R.id.day4))
                .perform(ViewActions.click())
        onView(withId(R.id.day7))
                .perform(ViewActions.click())
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
                ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
    }
}
