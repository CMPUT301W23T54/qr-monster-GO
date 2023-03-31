package com.example.qr_monster_go;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.LeaderboardsActivity;
import com.example.qr_monster_go.home.SearchUsersActivity;
import com.example.qr_monster_go.player.PlayerActivity;
import com.example.qr_monster_go.scan.ScanCodeActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomePageActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<HomePageActivity> rule =
            new ActivityTestRule<>(HomePageActivity.class, true, true);

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
        HomePageActivity activity = rule.getActivity();
    }

    /**
     * check if current activity moves to ScanCodeActivity
     */
    @Test
    public void checkScanCode() {
        // Asserts that the current activity is the HomePageActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);

//        solo.clickOnButton(R.id.scan_code_button);
        solo.clickOnView(solo.getView(R.id.scan_code_button));
        solo.waitForActivity(ScanCodeActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ScanCodeActivity.class);
    }

    /**
     * check if current activity moves to SearchUsersActivity
     */
    @Test
    public void checkSearchUsers() {
        // Asserts that the current activity is the HomePageActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);

        //solo.clickOnButton(R.id.search_users);
        solo.clickOnView(solo.getView(R.id.search_users));
        solo.waitForActivity(SearchUsersActivity.class);

        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);
    }

// Note: there is no MapLocationActivity yet
//    /**
//     * check if current activity moves to MapLocationActivity
//     */
//    @Test
//    public void checkMapLocation() {
//        // Asserts that the current activity is the HomePageActivity. Otherwise, show “Wrong Activity”
//        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
//
//        //solo.clickOnButton(R.id.map_location);
//        solo.clickOnView(solo.getView(R.id.map_location));
//        solo.waitForActivity("ShowActivity");
//        solo.assertCurrentActivity("Wrong Activity", MapLocationActivity.class);
//    }

    /**
     * check if current activity moves to LeaderboardsActivity
     */
    @Test
    public void checkLeaderboards() {
        // Asserts that the current activity is the HomePageActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);

//        solo.clickOnButton(R.id.leaderboards);
        solo.clickOnView(solo.getView(R.id.leaderboards));
        solo.waitForActivity(LeaderboardsActivity.class);
        solo.assertCurrentActivity("Wrong Activity", LeaderboardsActivity.class);
    }

    /**
     * check if current activity moves to PlayerActivity
     */
    @Test
    public void checkPlayer() {
        // Asserts that the current activity is the HomePageActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);

//        solo.clickOnButton(R.id.account_details);
        solo.clickOnView(solo.getView(R.id.account_details));
        solo.waitForActivity(PlayerActivity.class);
        solo.assertCurrentActivity("Wrong Activity", PlayerActivity.class);
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