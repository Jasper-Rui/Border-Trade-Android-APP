package com;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myfirstapp.LoginActivity;
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

//Tests need a little more refactoring - Fami
@RunWith(AndroidJUnit4.class)
public class MainFeedActivityTest {
    @Rule
    public ActivityScenarioRule<MainFeedActivity> myRule = new ActivityScenarioRule<>(MainFeedActivity.class);


    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @Test
    public void checkIfDisplayPostIsEmpty() {
        String input = "Random";
        onView(withId(R.id.searchPostsInput)).perform(typeText(input));
        onView(withId(R.id.searchPostsButton)).perform(click());
        onView(withId(R.id.postDescriptionTextView)).check(matches(withText("Sorry, no posts found for '" + input + "' :(")));
    }

    @Test
    public void checkIfNextPostButtonWorks() {
        onView(withId(R.id.nextPostButton)).perform(click());
        onView(withId(R.id.postTitleTextView)).check(matches(withText("Random2")));
    }

    @Test
     public void checkIfPrevPostButtonWorks() {
         onView(withId(R.id.prevPostButton)).perform(click());
         onView(withId(R.id.postTitleTextView)).check(matches(withText("Random1")));
     }

}