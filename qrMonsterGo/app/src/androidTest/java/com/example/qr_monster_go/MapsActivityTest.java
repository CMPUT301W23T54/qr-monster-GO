package com.example.qr_monster_go;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.maps.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MapsActivity> rule =
            new ActivityTestRule<>(MapsActivity.class, true, true);

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
        MapsActivity activity = rule.getActivity();
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkHome() {
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.clickOnView(solo.getView(R.id.back));
        solo.waitForActivity(HomePageActivity.class);
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    /**
     * Checks go to current
     */
    @Test
    public void checkCurrent() {
        solo.clickOnView(solo.getView(R.id.current));
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
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
