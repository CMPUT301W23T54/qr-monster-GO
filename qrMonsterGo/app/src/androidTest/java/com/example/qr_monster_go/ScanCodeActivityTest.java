package com.example.qr_monster_go;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.scan.ScanCodeActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScanCodeActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ScanCodeActivity> rule =
            new ActivityTestRule<>(ScanCodeActivity.class, true, true);

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
        ScanCodeActivity activity = rule.getActivity();
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