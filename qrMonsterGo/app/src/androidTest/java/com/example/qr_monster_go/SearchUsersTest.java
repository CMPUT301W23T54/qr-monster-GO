package com.example.qr_monster_go;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.ProfileActivity;
import com.example.qr_monster_go.home.SearchUsersActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Test class for SearchUsersTest. Robotium is used.
 */
public class SearchUsersTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<SearchUsersActivity> rule =
            new ActivityTestRule<>(SearchUsersActivity.class, true, true);

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
        Activity activity = rule.getActivity();
    }

    /**
     * Starts on the SearchUsers Activity, enters a name, clicks on the name,
     * and return back to search activity
     */
    @Test
    public void checkSearch() {
        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);

        solo.enterText((EditText) solo.getView(R.id.searched_user), "capybara");
        solo.clickOnView(solo.getView(R.id.search_users_button)); // Select SEARCH button
        solo.clearEditText((EditText) solo.getView(R.id.searched_user)); // Clear EditText

        // capybara is a user that is in the database
        assertTrue(solo.waitForText("capybara", 1, 2000));

//        solo.clickOnView(solo.getView(R.id.back_button)); // Exit player's profile
//        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);
    }

    /**
     * Checks a user's profile
     */
    @Test
    public void checkProfileActivity() {
        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);

        solo.enterText((EditText) solo.getView(R.id.searched_user), "capybara");
        solo.clickOnView(solo.getView(R.id.search_users_button)); // Select SEARCH button
        solo.clearEditText((EditText) solo.getView(R.id.searched_user)); // Clear EditText

        // capybara is a user that is in the database
        assertTrue(solo.waitForText("capybara", 1, 2000));

        solo.clickInList(0); // Select user
        solo.clickOnView(solo.getView(R.id.view_profile_button)); // Enter Profile

        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkReturn() {
        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);
        solo.clickOnView(solo.getView(R.id.return_button)); // Exit player's profile
        solo.waitForActivity(HomePageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    /**
     * Close activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
