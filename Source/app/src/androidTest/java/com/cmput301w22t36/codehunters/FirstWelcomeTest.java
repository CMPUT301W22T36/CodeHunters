package com.cmput301w22t36.codehunters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
//import com.robotium.solo.Solo;
//import android.test.ActivityInstrumentationTestCase2;

import com.cmput301w22t36.codehunters.Fragments.FirstWelcomeFragment;

/**
 * Class: FirstWelcomeTest
 * Test the FirstWelcomeFragment and MainActivity classes.
 * Espresso test framework is used.
 */
@RunWith(AndroidJUnit4.class)
public class FirstWelcomeTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void fragment_can_be_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FirstWelcomeFragment firstWelcome = startFirstWelcomeFragment();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    private FirstWelcomeFragment startFirstWelcomeFragment() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        FirstWelcomeFragment firstWelcome = new FirstWelcomeFragment();
        transaction.add(firstWelcome, "firstWelcome");
        transaction.commit();
        return firstWelcome;
    }
}
