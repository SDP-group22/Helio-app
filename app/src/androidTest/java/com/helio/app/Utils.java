package com.helio.app;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class Utils {
    public static Matcher<View> atPosition(
            final int position,
            @NonNull final Matcher<View> itemMatcher
    ) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> withExpectedCount(final int expectedCount) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {}

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                int count = view.getAdapter().getItemCount();
                return count == expectedCount;
            }
        };
    }

    public static Matcher<View> withText(final String expectedText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View item) {
                if(!(item instanceof TextInputLayout)) {
                    return false;
                }
                String text = ((TextInputLayout) item).getEditText().getText().toString();
                return expectedText.equals(text);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    // adapted from https://stackoverflow.com/a/38874162
    public static Matcher<View> withErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if(!(item instanceof TextInputLayout)) {
                    return false;
                }
                CharSequence error = ((TextInputLayout) item).getError();
                if(error == null) { // this happens when there is no error
                    return expectedErrorText.equals("");
                }
                return expectedErrorText.equals(error.toString());
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static int getCountFromRecyclerView(@IdRes int RecyclerViewId) {
        final int[] COUNT = {0};
        Matcher<View> matcher = new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                COUNT[0] = Objects.requireNonNull(((RecyclerView) item).getAdapter()).getItemCount();
                return true;
            }
            @Override
            public void describeTo(Description description) {}
        };
        onView(allOf(withId(RecyclerViewId), isDisplayed())).check(matches(matcher));
        return COUNT[0];
    }

    public static ViewAction clickOnViewChild(int viewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ViewActions.click().perform(uiController, view.findViewById(viewId));
            }
        };
    }

    public static void setHubIP(final String ip) {
        // navigate to settings fragment
        onView(withId(R.id.navigation_settings))
                .perform(ViewActions.click());
        // update the IP
        onView(withId(R.id.ip_address))
                .perform(ViewActions.click());
        onView(withId(R.id.ip_address_edittext))
                .perform(ViewActions.clearText())
                .perform(ViewActions.typeTextIntoFocusedView(ip));
        // attempt to connect
        onView(withId(R.id.connect_button))
                .perform(ViewActions.click());
    }
}
