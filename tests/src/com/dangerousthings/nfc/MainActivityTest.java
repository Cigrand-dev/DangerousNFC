package com.dangerousthings.nfc;

import com.dangerousthings.nfc.faketag.FakeNTAG;
import com.dangerousthings.nfc.faketag.FakeTagUtils;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super("com.dangerousthings.nfc", MainActivity.class);
    }

    public void test() throws Exception {
        FakeNTAG tag = new FakeNTAG();
        Intent intent = FakeTagUtils.createDiscoveryIntent(tag);

        getActivity().onNewIntent(intent);
    }
}
