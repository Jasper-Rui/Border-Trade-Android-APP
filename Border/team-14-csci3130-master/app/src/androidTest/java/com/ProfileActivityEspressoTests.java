package com;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myfirstapp.ManagePostsActivity;
import com.example.myfirstapp.PaypalActivity;
import com.example.myfirstapp.Profile;
import com.example.myfirstapp.R;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ProfileActivityEspressoTests {
/**
 * Instrumented test, which will execute on an Android device.
 */

    @Rule
    public ActivityScenarioRule<Profile> myRule = new ActivityScenarioRule<>(Profile.class);
    public IntentsTestRule<Profile> myIntentRule = new IntentsTestRule<>(Profile.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @Test
    public void checkIfPorfileIsShown() {

        onView(withId(R.id.profileButton)).perform(click());
        intended(hasComponent(Profile.class.getName()));
        Intents.release();


    }

    @Test
    public void checkIfPaymentIsShown() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.btnMakePayment)).perform(click());
        intended(hasComponent(PaypalActivity.class.getName()));
        Intents.release();

    }


    @Test
    public void checkIfUserAdvertisementsAreShowing(){
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.ManagePostsButton)).perform(click());
        intended(hasComponent(ManagePostsActivity.class.getName()));
        Intents.release();
    }

    /*

    //following test is not working yet, this is the test before coding
    //if user want to user other payment methond instead of paypal
    //user should be able to switch to others
    @Test
    public void checkIfGoogleIsShown() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.googlepay)).perform(click());
        intended(hasComponent(GooglepayActivity.class.getName()));
        Intents.release();
    }
    @Test
    public void checkIfGoogleIsShown() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.Alipay)).perform(click());
        intended(hasComponent(AliActivity.class.getName()));
        Intents.release();
    }
    @Test
    public void checkIfGoogleIsShown() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.Applepay)).perform(click());
        intended(hasComponent(AppleActivity.class.getName()));
        Intents.release();
    }
*/






}
