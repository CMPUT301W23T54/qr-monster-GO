package com.example.qr_monster_go;

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


// note: sign up process can only be tested at the first time(uninstall app needed if already ran before), we can't run test it again and again.
//    @Test
//    public void checkSignup() {
//        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        //Get view for EditText and enter a user name
//        solo.enterText((EditText) solo.getView(R.id.username_entry), "Apple");
//        solo.clickOnButton(R.id.sign_up);//Select CONFIRM Button
//        //solo.clearEditText((EditText) solo.getView(R.id.editText_name));//Clear the EditText
//
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);;
//    }



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