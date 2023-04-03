package com.example.qr_monster_go;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.qr_monster_go.scan.ImageCaptureActivity;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ImageCaptureActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ImageCaptureActivity> rule =
            new ActivityTestRule<>(ImageCaptureActivity.class, true, true);

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
        ImageCaptureActivity activity = rule.getActivity();
    }

    /**
     * Checks return to Home page activity
     */
    @Test
    public void checkTakePicture() {
        solo.clickOnView(solo.getView(R.id.take_picture_button));
        solo.assertCurrentActivity("Wrong Activity", ImageCaptureActivity.class);
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
