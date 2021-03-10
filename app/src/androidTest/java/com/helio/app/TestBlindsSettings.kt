package com.helio.app

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.helio.app.Utils.getCountFromRecyclerView
import com.helio.app.Utils.withExpectedCount
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestBlindsSettings {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        // navigate to blinds settings page
        onView(withId(R.id.navigation_blinds))
                .perform(click());
    }

    @Test
    // check that we can navigate into a single blind settings fragment
    fun enterBlind0Settings() {
        onView(withId(R.id.blindsRCView))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, click()));
        // in single blinds settings fragment
        onView(withId(R.id.btn_open)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_close)).check(matches(isDisplayed()))
    }

    @Test
    // check that we can add a new blind using the "+"-button
    fun registerNewBlind() {
        val startCount = getCountFromRecyclerView(R.id.blindsRCView)
        onView(withId(R.id.add_blinds_button))
                .perform(click())
        onView(withId(R.id.blindsRCView))
                .check(matches(withExpectedCount(startCount + 1)));
    }
}
