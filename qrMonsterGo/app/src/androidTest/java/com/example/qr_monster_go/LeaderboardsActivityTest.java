package com.example.qr_monster_go;

import static org.junit.Assert.assertTrue;

import android.widget.Adapter;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.home.LeaderboardsActivity;
import com.example.qr_monster_go.home.SearchUsersActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LeaderboardsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LeaderboardsActivity> rule =
            new ActivityTestRule<>(LeaderboardsActivity.class, true, true);

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
        LeaderboardsActivity activity = rule.getActivity();
    }

    /**
     * Checks Leaderboards shows information
     */
    @Test
    public void checkLoadLeaders() {
        solo.assertCurrentActivity("Wrong Activity", LeaderboardsActivity.class);
        ListView view = (ListView) solo.getView(R.id.players);
        // have to call below to get data populated in listview
        solo.clickInList(0);
        int count = view.getAdapter().getCount();
        assertTrue(count > 0);
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkReturn() {
        solo.assertCurrentActivity("Wrong Activity", LeaderboardsActivity.class);
        solo.clickOnView(solo.getView(R.id.home_button2)); // Exit player's profile
        solo.waitForActivity(HomePageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
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