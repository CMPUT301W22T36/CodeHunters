package com.cmput301w22t36.codehunters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class: ProfileAndEditTest
 * Test the UserPersonalProfileFragment, EditEmailFragment, and EditNameFragment classes.
 * Espresso test framework is used.
 */
@RunWith(AndroidJUnit4.class)
public class ProfileAndEditTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void fragment1_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserPersonalProfileFragment profile = startFrag1();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void fragment2_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditEmailFragment editE = startFrag2();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void fragment3_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditNameFragment editN = startFrag3();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    private UserPersonalProfileFragment startFrag1() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        UserPersonalProfileFragment profile = new UserPersonalProfileFragment();
        transaction.add(profile, "profile");
        transaction.commit();
        return profile;
    }

    private EditEmailFragment startFrag2() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        EditEmailFragment editE = new EditEmailFragment();
        transaction.add(editE, "editE");
        transaction.commit();
        return editE;
    }

    private EditNameFragment startFrag3() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        EditNameFragment editN = new EditNameFragment();
        transaction.add(editN, "editN");
        transaction.commit();
        return editN;
    }

}