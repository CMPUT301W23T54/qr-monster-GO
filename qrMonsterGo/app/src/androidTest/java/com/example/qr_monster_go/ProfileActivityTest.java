package com.example.qr_monster_go;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.LeaderboardsActivity;
import com.example.qr_monster_go.home.ProfileActivity;
import com.example.qr_monster_go.home.SearchUsersActivity;
import com.example.qr_monster_go.player.ScannedPlayersActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<ProfileActivity>(ProfileActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, ProfileActivity.class);
                    result.putExtra("username", "dog");
                    return result;
                }
            };

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
        ProfileActivity activity = rule.getActivity();
    }

    /**
     * Checks return to search users activity
     */
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(SearchUsersActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchUsersActivity.class);
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkHome() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.home_button));
        solo.waitForActivity(HomePageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }
    /**
     *
     * Checks return to Scanned players activity
     */
    @Test
    public void checkScannedPlayers() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickInList(0); // select the first code
        solo.clickOnView(solo.getView(R.id.view_code_button));
        solo.waitForActivity(ScannedPlayersActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ScannedPlayersActivity.class);
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