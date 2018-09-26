package com.juancoob.nanodegree.and.vegginner.ui;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VegginnerUITests {

    private CountingIdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getCountingIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void selectRecipe_checkIngredients_comeBack() {
        // If drawer is closed, open it
        onView(withId(R.id.dl_main)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        // Select recipes
        onView(withId(R.id.nv_main)).perform(NavigationViewActions.navigateTo(R.id.nav_recipes));

        // Select the first recipe
        onView(withId(R.id.rv_recipes)).perform(actionOnItemAtPosition(0, click()));

        // Check if the list has 5 elements
        onView(withId(R.id.rv_ingredients)).check(matches(hasMinimumChildCount(5)));

        // Check title
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.t_recipe_toolbar)))).check(matches(withText(containsString("Vegan Caramel"))));

        // Go back
        onView((withContentDescription("Come back to the main list"))).perform(click());

    }
}
