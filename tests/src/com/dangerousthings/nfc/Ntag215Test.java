package com.dangerousthings.nfc;

import com.dangerousthings.nearfield.fake.FakeNtag215;
import com.dangerousthings.nearfield.utils.DispatchUtils;

import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;

import com.robotium.solo.Solo;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;

public class Ntag215Test extends ActivityInstrumentationTestCase2<MainActivity> {
    Solo solo;
    MainActivity activity;

    public Ntag215Test() {
        super("com.dangerousthings.nfc", MainActivity.class);
    }

    public void setUp() throws Exception {
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
    }

    public void testVirginTag() throws Exception {
        String test_password_string = RandomStringUtils.randomAlphanumeric(4);
        byte[] test_password_bytes = test_password_string.getBytes(Charset.forName("UTF-8"));
        activity.setPasswordBytes(test_password_bytes);

        FakeNtag215 tag = FakeNtag215.newInstance();

        activity.onNewIntent(DispatchUtils.createDiscoveryIntent(tag));

        solo.waitForDialogToOpen();
        assertTrue(solo.searchText("Success! Tag has been updated! :)"));
        solo.clickOnButton("Okay");

        MoreAsserts.assertEquals(tag.getCC(), MainActivity.NEW_NTAG215_CC);
        MoreAsserts.assertEquals(tag.getStaticLockBytes(), MainActivity.NEW_STATIC_LOCK_BYTES);
        MoreAsserts.assertEquals(tag.getDynamicLockBytes(), MainActivity.NEW_NTAG215_DYNAMIC_LOCK_BYTES);
        MoreAsserts.assertEquals(tag.getPwd(), test_password_bytes);
        MoreAsserts.assertEquals(tag.getPack(), MainActivity.NEW_PACK);
        assertEquals(tag.getAuth0(), MainActivity.NEW_NTAG215_AUTH0);
    }

    public void testTagWithBadCC() throws Exception {
        String test_password_string = RandomStringUtils.randomAlphanumeric(4);
        byte[] test_password_bytes = test_password_string.getBytes(Charset.forName("UTF-8"));
        activity.setPasswordBytes(test_password_bytes);

        FakeNtag215 tag = FakeNtag215.newInstance();
        tag.setCC(new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF });

        activity.onNewIntent(DispatchUtils.createDiscoveryIntent(tag));

        solo.waitForDialogToOpen();
        assertTrue(solo.searchText("Error: Bad Capability Container"));
        solo.clickOnButton("Okay");
    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
