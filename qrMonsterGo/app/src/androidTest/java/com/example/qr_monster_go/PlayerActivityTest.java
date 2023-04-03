package com.example.qr_monster_go;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.ProfileActivity;
import com.example.qr_monster_go.home.SearchUsersActivity;
import com.example.qr_monster_go.player.PlayerActivity;
import com.example.qr_monster_go.player.ScannedPlayersActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PlayerActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<PlayerActivity> rule =
            new ActivityTestRule<PlayerActivity>(PlayerActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, PlayerActivity.class);
                    result.putExtra("username", "player456");
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
        PlayerActivity activity = rule.getActivity();
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkHome() {
        solo.assertCurrentActivity("Wrong Activity", PlayerActivity.class);
        solo.clickOnView(solo.getView(R.id.home_return));
        solo.waitForActivity(HomePageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    /**
     * Checks players scanned the code
     */
    @Test
    public void checkPlayersScanned() {
        solo.assertCurrentActivity("Wrong Activity", PlayerActivity.class);
        solo.clickInList(0); // select the first code
        solo.clickOnView(solo.getView(R.id.players_scanned_button));
        solo.waitForActivity(ScannedPlayersActivity.class);
        solo.assertCurrentActivity("Wrong Activity", ScannedPlayersActivity.class);
    }

    /**
     * Checks delete player's codes functionality
     */
    @Test
    public void checkDeleteCode() {
        ListView view = (ListView) solo.getView(R.id.codes);
        solo.clickInList(0); // select the first code
        int count = view.getAdapter().getCount(); // count how many code now
        solo.clickOnView(solo.getView(R.id.delete_button));
        solo.clickInList(0); // select the first code
        assertTrue(view.getAdapter().getCount() == count - 1); // check if the counter decrease by 1
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