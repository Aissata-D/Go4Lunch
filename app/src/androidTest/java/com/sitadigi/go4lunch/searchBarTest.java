package com.sitadigi.go4lunch;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class searchBarTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");
/*
    @Test
    public void searchBarTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(androidx.appcompat.R.id.email_button), withText("Se connecter avec une adresse e-mail"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.btn_holder),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.container),
                                                0)),
                                0)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(androidx.appcompat.R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.email_layout),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("testinstrumented@yahoo.com"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(androidx.appcompat.R.id.button_next), withText("Suivant"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(androidx.appcompat.R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.name_layout),
                                        0),
                                0)));
        textInputEditText2.perform(scrollTo(), click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(androidx.appcompat.R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.name_layout),
                                        0),
                                0)));
        textInputEditText3.perform(scrollTo(), replaceText("TestName"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(androidx.appcompat.R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.password_layout),
                                        0),
                                0)));
        textInputEditText4.perform(scrollTo(), replaceText("retest"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(androidx.appcompat.R.id.button_create), withText("Enregistrer"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.toolbar_search_bar), withContentDescription("image search bar"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout3),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(androidx.appcompat.R.id.places_autocomplete_search_input),
                        childAtPosition(
                                allOf(withId(R.id.autocomplete_fragment),
                                        childAtPosition(
                                                withId(R.id.autocomplete_cardview),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withParent(withParent(withId(androidx.appcompat.R.id.places_autocomplete_content))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

 */
}
