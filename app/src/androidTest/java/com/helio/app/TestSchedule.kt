package com.helio.app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition

import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest

import com.google.android.material.slider.Slider
import com.helio.app.Utils.atPosition
import org.hamcrest.Description
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestSchedule {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        // navigate to the desired fragment
        onView(withId(R.id.navigation_schedule))
                .perform(ViewActions.click())
    }

    @Test
    // check that we can toggle the switch for schedule #1
    fun toggleSwitch0() {
        onView(withId(R.id.schedulesRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, clickOnViewChild(R.id.activate_switch)))
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
    // check that we can add a new blind using the "+"-button
    fun registerNewSchedule() {
        val startCount = Utils.getCountFromRecyclerView(R.id.schedulesRCView)
        onView(withId(R.id.add_button))
                .perform(ViewActions.click())
        onView(withId(R.id.schedulesRCView))
                .check(matches(Utils.withExpectedCount(startCount + 1)));
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
                ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
    }
}
