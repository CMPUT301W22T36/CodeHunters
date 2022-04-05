package com.cmput301w22t36.codehunters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cmput301w22t36.codehunters.Fragments.EditEmailFragment;
import com.cmput301w22t36.codehunters.Fragments.EditNameFragment;
import com.cmput301w22t36.codehunters.Fragments.UserPersonalProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class: AccountInitializationTest
 * Test the account creation activity navigation in the MainActivity and FirstWelcomeActivity classes.
 * Espresso test framework is used.
 */
@RunWith(AndroidJUnit4.class)
public class AccountInitializationTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void testAIT_fragment1_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserPersonalProfileFragment profile = testAIT_startFrag1();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void testAIT_fragment2_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditEmailFragment editE = testAIT_startFrag2();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void testAIT_fragment3_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditNameFragment editN = testAIT_startFrag3();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    private UserPersonalProfileFragment testAIT_startFrag1() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        UserPersonalProfileFragment profile = new UserPersonalProfileFragment();
        transaction.add(profile, "profile");
        transaction.commit();
        return profile;
    }

    private EditEmailFragment testAIT_startFrag2() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        EditEmailFragment editE = new EditEmailFragment();
        transaction.add(editE, "editE");
        transaction.commit();
        return editE;
    }

    private EditNameFragment testAIT_startFrag3() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        EditNameFragment editN = new EditNameFragment();
        transaction.add(editN, "editN");
        transaction.commit();
        return editN;
    }

}