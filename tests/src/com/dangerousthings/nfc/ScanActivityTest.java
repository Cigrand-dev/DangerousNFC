package com.dangerousthings.nfc;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.dangerousthings.nfc.ScanActivityTest \
 * com.dangerousthings.nfc.tests/android.test.InstrumentationTestRunner
 */
public class ScanActivityTest extends ActivityInstrumentationTestCase2<ScanActivity> {

    public ScanActivityTest() {
        super("com.dangerousthings.nfc", ScanActivity.class);
    }

}
