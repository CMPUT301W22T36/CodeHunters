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
import com.cmput301w22t36.codehunters.Fragments.SearchNearbyCodesFragment;
import com.cmput301w22t36.codehunters.Fragments.UserPersonalProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class: SidebarNavigationTest
 * Test the sidebar navigation in the MainActivity, MenuHeader, UserPersonalProfileFragment, and
 * SearchNearbyCodesFragment classes.
 * Espresso test framework is used.
 */
@RunWith(AndroidJUnit4.class)
public class SidebarNavigationTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void testSNT_fragment1_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserPersonalProfileFragment profile = testSNT_startFrag1();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    @Test
    public void testSNT_fragment2_instantiated() {
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchNearbyCodesFragment searchNCF = testSNT_startFrag2();
            }
        });
        // Then use Espresso to test the Fragment
        onView(withId(R.id.title)).check(matches(isDisplayed()));
    }

    private UserPersonalProfileFragment testSNT_startFrag1() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        UserPersonalProfileFragment profile = new UserPersonalProfileFragment();
        transaction.add(profile, "profile");
        transaction.commit();
        return profile;
    }

    private SearchNearbyCodesFragment testSNT_startFrag2() {
        MainActivity activity = (MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        SearchNearbyCodesFragment searchNCF = new SearchNearbyCodesFragment();
        transaction.add(searchNCF, "searchNCF");
        transaction.commit();
        return searchNCF;
    }

}