package com.dangerousthings.nfc;

import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;

import com.robotium.solo.Solo;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    Solo solo;
    MainActivity activity;

    public MainActivityTest() {
        super("com.dangerousthings.nfc", MainActivity.class);
    }

    public void setUp() throws Exception {
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
    }

    public void testSetPassword() throws Exception {
        assertNull(activity.getPasswordBytes());

        String test_password_string = RandomStringUtils.randomAlphanumeric(4);
        byte[] test_password_bytes = test_password_string.getBytes(Charset.forName("UTF-8"));

        solo.clickOnButton("Select Password");
        solo.waitForDialogToOpen();
        solo.typeText(0, test_password_string);
        solo.clickOnButton("Save");
        solo.waitForDialogToClose();

        MoreAsserts.assertEquals(test_password_bytes, activity.getPasswordBytes());
    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
