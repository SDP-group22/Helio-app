package com.helio.app

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition

import org.hamcrest.Matcher
import org.junit.Before
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
import org.hamcrest.Description

@RunWith(AndroidJUnit4::class)
@LargeTest
class TestBlindsControl {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java);

    @Before
    // ensure the fragment contains blinds objects
    fun initBlindsList() {

    }

    @Test
    fun changeText_sameActivity() {
        onView(withId(R.id.control_rc_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, setValue(1.0F)))
    }


//    fun withValue(expectedValue: Float): Matcher<View?> {
//        return object : BoundedMatcher<View?, Slider>(Slider::class.java) {
//            override fun describeTo(description: Description) {
//                description.appendText("expected: $expectedValue")
//            }
//
//            override fun matchesSafely(slider: Slider?): Boolean {
//                return slider?.value == expectedValue
//            }
//        }
//    }

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
}
