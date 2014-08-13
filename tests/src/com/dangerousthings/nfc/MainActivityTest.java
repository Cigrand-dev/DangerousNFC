package com.dangerousthings.nfc;

import com.dangerousthings.nfc.faketag.FakeNTAG;
import com.dangerousthings.nfc.faketag.FakeTagUtils;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;

import com.robotium.solo.Solo;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {
        super("com.dangerousthings.nfc", MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testPassword() throws Exception {
        MainActivity activity = getActivity();

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

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
