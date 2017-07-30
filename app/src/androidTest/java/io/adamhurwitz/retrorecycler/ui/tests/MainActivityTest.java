package io.adamhurwitz.retrorecycler.ui.tests;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import io.github.adamshurwitz.retrorecycler.MainActivity;
import io.github.adamshurwitz.retrorecycler.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ahurwitz on 6/11/17.
 */

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testScreenLoad() {
        onView(withText("RetrofitRecycler")).check(matches(isDisplayed()));
    }

    //todo: debug
    @Test
    public void scrollToCourseCatalog() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.recyclerList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // Match the text in an item below the fold and check that it's displayed.
        onView(withText("COURSE CATALOG")).check(matches(isDisplayed()));
    }

}

