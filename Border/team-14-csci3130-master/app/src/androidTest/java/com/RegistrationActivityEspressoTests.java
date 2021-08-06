package com;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myfirstapp.MainFeedActivity;
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
public class RegistrationActivityEspressoTests {
    @Rule
    public ActivityScenarioRule<RegisterActivity> myRule = new ActivityScenarioRule<>(RegisterActivity.class);
    public IntentsTestRule<RegisterActivity> myIntentRule = new IntentsTestRule<>(RegisterActivity.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }


    @Test
    public void checkIfFullNameIsEmpty() {
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("abc123@dal.ca"));
        onView(withId(R.id.passwordInput)).perform(typeText("Password!!"));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.statuslabelforregister)).check(matches(withText(R.string.EMPTY_FIELDS)));
    }
    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.editTextTextPersonName)).perform(typeText("John Doe"));
        onView(withId(R.id.passwordInput)).perform(typeText("Password!!"));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.statuslabelforregister)).check(matches(withText(R.string.EMPTY_FIELDS)));
    }
    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.editTextTextPersonName)).perform(typeText("John Doe"));
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("abc123@dal.ca"));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.statuslabelforregister)).check(matches(withText(R.string.EMPTY_FIELDS)));
    }

    //added at Feb 3rd
    @Test
    public void checkIfEmailIsInvalid() {
        onView(withId(R.id.editTextTextPersonName)).perform(typeText("John Doe"));
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("abc123.dal.ca"));
        onView(withId(R.id.passwordInput)).perform(typeText("Password!!"));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.statuslabelforregister)).check(matches(withText("Please check you email format")));
    }

 /*       @Test //currently failed because regex for invalid has not beed added on, will work on this one later feb 2nd
        public void checkIfPasswordInValid(){
        onView(withId(R.id.editTextTextPersonName)).perform(typeText("Haorui"));
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("haorui@gmail.com"));
        onView(withId(R.id.passwordInput)).perform(typeText("1234567890"));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.statuslabelforregister)).check(matches(withText("Invalid password")));
    }

    @Test
    public void checkIfMove2MainFeedPage(){
        onView(withId(R.id.editTextTextPersonName)).perform(typeText("Haoruii"));
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("haui@gmail.com"));
        onView(withId(R.id.passwordInput)).perform(typeText("Haori12356@"));
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent(MainFeedActivity.class.getName()));
        Intents.release();
    }*/

}
