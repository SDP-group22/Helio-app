package com.helio.app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.slider.Slider
import com.helio.app.Utils.atPosition
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class TestBlindsControl {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun connectIp() {
            val scenario = ActivityScenario.launch(MainActivity::class.java)
            // otherwise the elements won't load
            Utils.setHubIP("10.0.2.2")
            scenario.close()
        }
    }

    @Before
    fun setup() {
        // navigate to the desired fragment
        onView(withId(R.id.navigation_control))
                .perform(ViewActions.click())
    }

    @Test
    // check that we can adjust the slider for the first blind in the list
    fun adjustSlider0() {
        val sliderPosition = 0
        // adjust to 50%
        var expectedValue = 50.0F
        onView(withId(R.id.control_rc_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (sliderPosition, setValue(expectedValue)))
        onView(withId(R.id.control_rc_view))
                .check(matches(atPosition(sliderPosition, withValue(expectedValue))))
        // adjust to 10%
        expectedValue = 10.0F
        onView(withId(R.id.control_rc_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (sliderPosition, setValue(expectedValue)))
        onView(withId(R.id.control_rc_view))
                .check(matches(atPosition(sliderPosition, withValue(expectedValue))))
    }

    @Test
    // check that we can adjust multiple sliders
    fun adjustMultipleSliders() {
        val slider1Position = 0
        val slider2Position = 1
        val slider1ExpectedValue = 100.0F
        val slider2ExpectedValue = 33.0F
        // adjust slider 1 to 100%
        onView(withId(R.id.control_rc_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (slider1Position, setValue(slider1ExpectedValue)))
        // adjust slider 2 to 33.2%
        onView(withId(R.id.control_rc_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (slider2Position, setValue(slider2ExpectedValue)))
        // verify values match expectations
        onView(withId(R.id.control_rc_view))
                .check(matches(atPosition(slider1Position, withValue(slider1ExpectedValue))))
        onView(withId(R.id.control_rc_view))
                .check(matches(atPosition(slider2Position, withValue(slider2ExpectedValue))))
    }

    @Test
    // check that we can navigate into a single blind settings fragment
    fun enterBlind0Settings() {
        onView(withId(R.id.control_rc_view))
                // click on the icon (instead of the centre) to avoid pressing the slider
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, clickOnViewChild(R.id.blindIcon)))
        // check that the calibration button is displayed to confirm we're in settings fragment
        onView(withId(R.id.calibration)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    // check that we can add a new blind using the "+"-button
    fun registerNewBlind() {
        val startCount = Utils.getCountFromRecyclerView(R.id.control_rc_view)
        onView(withId(R.id.add_blinds_button))
                .perform(ViewActions.click())
        // exit from new blind's settings
        Thread.sleep(500)
        Espresso.pressBack()
        onView(withId(R.id.control_rc_view))
                .check(matches(Utils.withExpectedCount(startCount + 1)))
    }

    private fun withValue(expectedValue: Float): Matcher<View?> {
        return object : BoundedMatcher<View?, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("expected: $expectedValue")
            }

            override fun matchesSafely(view: View): Boolean {
                return view.findViewById<Slider>(R.id.controlSlider).value == expectedValue
            }
        }
    }

    fun setValue(value: Float): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view.findViewById(R.id.controlSlider) as Slider
                seekBar.value = value
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
                ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
    }
}
