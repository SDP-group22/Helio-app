package com.helio.app

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestSettings {

    // hardcode these values because grabbing them from R.strings doesn't work here
    private val defaultThemeName = "Light"
    private val nightThemeName = "Night"
    private val highContrastThemeName = "High Contrast"

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        // navigate to the desired fragment
        onView(withId(R.id.navigation_settings))
                .perform(ViewActions.click())
        // set default theme to have predictable state in the tests
        switchTheme(defaultThemeName)
    }

    @Test
    // check that we can switch to night theme
    fun switchThemeNight() {
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText("Light")))
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

    private fun switchTheme(themeName: String) {
        Thread.sleep(2000)
        onView(withId(R.id.theme_menu))
                .perform(ViewActions.click())
        Thread.sleep(2000)
        onView(withText(themeName))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click())
        Thread.sleep(2000)
    }
}
