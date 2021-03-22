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
class TestSchedule {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        // otherwise the elements won't load
        Utils.setHubIP("10.0.2.2")
        // navigate to the desired fragment
        onView(withId(R.id.navigation_schedule))
                .perform(ViewActions.click())
    }

    @Test
    // check that we can add a new schedule using the "+"-button
    fun registerNewSchedule() {
        val startCount = Utils.getCountFromRecyclerView(R.id.schedulesRCView)
        onView(withId(R.id.add_button))
                .perform(ViewActions.click())
        // exit from new schedule's settings
        Espresso.pressBack()
        onView(withId(R.id.schedulesRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    @Test
    // check that we can toggle the switch for schedule #1
    fun toggleSwitch0() {
        onView(withId(R.id.schedulesRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, Utils.clickOnViewChild(R.id.activate_switch)))
    }

    @Test
    // check that we can navigate into a single schedule settings fragment
    fun enterSchedule0Settings() {
        onView(withId(R.id.schedulesRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, ViewActions.click()))
        // check that the time button is displayed to confirm we're in settings fragment
        onView(withId(R.id.time_button)).check(matches(ViewMatchers.isDisplayed()))
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
}
