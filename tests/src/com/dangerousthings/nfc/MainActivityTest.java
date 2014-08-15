package com.dangerousthings.nfc;

import com.dangerousthings.nearfield.fake.FakeNtag216;
import com.dangerousthings.nearfield.fake.FakeTagService;
import com.dangerousthings.nearfield.fake.TagService;
import com.dangerousthings.nearfield.reflect.TagReflection;
import com.dangerousthings.nearfield.utils.DispatchUtils;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;

import com.robotium.solo.Solo;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    MainActivity activity;
    TagService tag_service = FakeTagService.getInstance();

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

    public void testUnalteredTag() throws Exception {
        String test_password_string = RandomStringUtils.randomAlphanumeric(4);
        byte[] test_password_bytes = test_password_string.getBytes(Charset.forName("UTF-8"));
        activity.setPasswordBytes(test_password_bytes);

        FakeNtag216 tag = FakeNtag216.newInstance();
        tag_service.add(tag);

        Intent intent = DispatchUtils.createDiscoveryIntent(tag_service, tag);
        activity.onNewIntent(intent);
    }

    // public void testTagWithBadCC() throws Exception {
    // }

    // public void testTagWithBadDynamicLockBytes() throws Exception {
    // }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
