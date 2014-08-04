package com.dangerousthings.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.test.ActivityInstrumentationTestCase2;

import android.util.Log;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    xNT mTag;
    StubNTAG mStubNTAG;
    MainActivity mMainActivity;

    public MainActivityTest() {
        super("com.dangerousthings.nfc", MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();

        byte[] uid = { (byte) 0x04, (byte) 0xE9, (byte) 0x18, (byte) 0x2A, (byte) 0xE0, (byte) 0x35, (byte) 0x80 };
        mStubNTAG = new StubNTAG(uid);
        mTag = new xNT(mStubNTAG.tag);

        Intent intent = new Intent();
        intent.setAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(NfcAdapter.EXTRA_TAG, mStubNTAG.tag);

        mMainActivity.onNewIntent(intent);
    }

    public void testMemberVariables() {
        assertNotNull("mTag is null", mTag);
        assertNotNull("mStubNTAG is null", mStubNTAG);
        assertNotNull("mMainActivity is null", mMainActivity);
    }
}
