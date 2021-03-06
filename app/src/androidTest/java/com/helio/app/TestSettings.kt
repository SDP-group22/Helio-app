package com.helio.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
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
    // check that we can switch to default theme (nothing should change here)
    fun switchThemeDefault() {
        // verify starting theme
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(defaultThemeName)))
        // switch to new theme
        switchTheme(defaultThemeName)
        // verify updated theme name in dropdown
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(defaultThemeName)))
    }

    @Test
    // check that we can switch to night theme
    fun switchThemeNight() {
        // verify starting theme
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(defaultThemeName)))
        // switch to new theme
        switchTheme(nightThemeName)
        // verify updated theme name in dropdown
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(nightThemeName)))
    }

    @Test
    // check that we can switch to high contrast theme
    fun switchThemeHighContrast() {
        // verify starting theme
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(defaultThemeName)))
        // switch to new theme
        switchTheme(highContrastThemeName)
        // verify updated theme name in dropdown
        onView(withId(R.id.theme_menu))
                .check(matches(Utils.withText(highContrastThemeName)))
    }

    @Test
    // check that we can update the IP address value to a new, correctly formatted IP
    fun updateHubIPValid() {
        val ip = "1.2.8.9"
        Utils.setHubIP(ip)
        // check that IP is correctly set
        onView(withId(R.id.ip_address))
                .check(matches(Utils.withText(ip)))
        // verify that the app also thinks the format is correct
        onView(withId(R.id.ip_address))
                .check(matches(Utils.withErrorText("")))
    }

    @Test
    // check that providing an invalid IP format gives an error
    fun updateHubIPInvalid() {
        val ip = "2000.0.256.9"
        Utils.setHubIP(ip)
        // check that IP is correctly set
        onView(withId(R.id.ip_address))
                .check(matches(Utils.withText(ip)))
        // verify that the app also thinks the format is correct
        onView(withId(R.id.ip_address))
                .check(matches(Utils.withErrorText("Incorrect format")))
    }

    private fun switchTheme(themeName: String) {
        onView(withId(R.id.theme_menu))
                .perform(ViewActions.click())
        onView(withText(themeName))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click())
    }
}
