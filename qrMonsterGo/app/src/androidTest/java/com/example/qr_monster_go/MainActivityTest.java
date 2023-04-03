package com.example.qr_monster_go;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.MainActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        MainActivity activity = rule.getActivity();
    }


    /**
     * Check a user sign up
     */
    @Test
    public void checkSignup() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Get view for EditText and enter a user name
        solo.enterText((EditText) solo.getView(R.id.username_entry), "testcases");
        solo.enterText((EditText) solo.getView(R.id.phone_entry), "1231231234");
        solo.enterText((EditText) solo.getView(R.id.email_entry), "testing@gmail.com");
        solo.clickOnView(solo.getView(R.id.sign_up));//Select Button
        solo.clearEditText((EditText) solo.getView(R.id.username_entry));//Clear the EditText
        solo.clearEditText((EditText) solo.getView(R.id.phone_entry));
        solo.clearEditText((EditText) solo.getView(R.id.email_entry));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Closes the activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}