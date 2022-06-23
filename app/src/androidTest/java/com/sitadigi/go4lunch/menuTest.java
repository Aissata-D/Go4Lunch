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
public class menuTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");
/*
    @Test
    public void menuTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(com.firebase.ui.auth.R.id.email_button), withText("Se connecter avec une adresse e-mail"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.btn_holder),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.container),
                                                0)),
                                0)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(com.firebase.ui.auth.R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.firebase.ui.auth.R.id.email_layout),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("test@yahoo.com"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(com.firebase.ui.auth.R.id.button_next), withText("Suivant"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(com.firebase.ui.auth.R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.firebase.ui.auth.R.id.name_layout),
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
        textInputEditText3.perform(scrollTo(), replaceText("Test"), closeSoftKeyboard());

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

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Ouvrir le panneau de navigation"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(androidx.appcompat.R.id.design_navigation_view),
                        withParent(allOf(withId(R.id.nav_view),
                                withParent(withId(R.id.drawer_layout)))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction checkedTextView = onView(
                allOf(withId(androidx.appcompat.R.id.design_menu_item_text), withText("DECONNEXION"),
                        withParent(allOf(withId(R.id.nav_slideshow),
                                withParent(withId(androidx.appcompat.R.id.design_navigation_view)))),
                        isDisplayed()));
        checkedTextView.check(matches(isDisplayed()));

        ViewInteraction checkedTextView2 = onView(
                allOf(withId(androidx.appcompat.R.id.design_menu_item_text), withText("PARAMETRES"),
                        withParent(allOf(withId(R.id.nav_gallery),
                                withParent(withId(androidx.appcompat.R.id.design_navigation_view)))),
                        isDisplayed()));
        checkedTextView2.check(matches(isDisplayed()));

        ViewInteraction checkedTextView3 = onView(
                allOf(withId(androidx.appcompat.R.id.design_menu_item_text), withText("VOTRE REPAS"),
                        withParent(allOf(withId(R.id.nav_home),
                                withParent(withId(androidx.appcompat.R.id.design_navigation_view)))),
                        isDisplayed()));
        checkedTextView3.check(matches(isDisplayed()));

        ViewInteraction viewGroup = onView(
                allOf(withParent(allOf(withId(androidx.appcompat.R.id.navigation_header_container),
                                withParent(withId(androidx.appcompat.R.id.design_navigation_view)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withText("Go4Lunch"),
                        withParent(withParent(withId(androidx.appcompat.R.id.navigation_header_container))),
                        isDisplayed()));
        textView.check(matches(withText("Go4Lunch")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.img_user), withContentDescription("Navigation header"),
                        withParent(withParent(withId(androidx.appcompat.R.id.navigation_header_container))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.img_user), withContentDescription("Navigation header"),
                        withParent(withParent(withId(androidx.appcompat.R.id.navigation_header_container))),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.name_user), withText("Test"),
                        withParent(withParent(withId(androidx.appcompat.R.id.navigation_header_container))),
                        isDisplayed()));
        textView2.check(matches(withText("Test")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.email_user), withText("test@yahoo.com"),
                        withParent(withParent(withId(androidx.appcompat.R.id.navigation_header_container))),
                        isDisplayed()));
        textView3.check(matches(withText("test@yahoo.com")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(androidx.appcompat.R.id.design_navigation_view),
                        withParent(allOf(withId(R.id.nav_view),
                                withParent(withId(R.id.drawer_layout)))),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
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
