package com;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myfirstapp.LoginActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.RegisterActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityEspressoTests {
    @Rule
    public ActivityScenarioRule<LoginActivity> myRule = new ActivityScenarioRule<>(LoginActivity.class);
    public IntentsTestRule<LoginActivity> myIntentRule = new IntentsTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @Test
    public void checkIfLoginPageIsShown() {
        onView(ViewMatchers.withId(R.id.email)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.password)).check(matches(withText(R.string.EMPTY_STRING)));
        onView(withId(R.id.loginButton)).check(matches(withText("Login")));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.email)).perform(typeText(""));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusLabelPost)).check(matches(withText(R.string.EMPTY_EMAIL_ADDRESS)));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.email)).perform(typeText("abc@email.com"));
        onView(withId(R.id.password)).perform(typeText(""));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusLabelPost)).check(matches(withText(R.string.EMPTY_PASSWORD)));
    }

    @Test //if you run the whole test this one will not work, but if you only run this test, it will work
    public void checkIfMoved2RegistrationPage(){
        onView(withId(R.id.moveToRegisterButton)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
        Intents.release();
    }
}
