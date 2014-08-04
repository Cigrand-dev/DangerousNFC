package com.dangerousthings.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.test.ActivityInstrumentationTestCase2;

import android.util.Log;

public class ScanActivityTest extends ActivityInstrumentationTestCase2<ScanActivity> {
    xNT mTag;
    StubNTAG mStubNTAG;
    ScanActivity mScanActivity;

    public ScanActivityTest() {
        super("com.dangerousthings.nfc", ScanActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mScanActivity = getActivity();

        byte[] uid = { (byte) 0x04, (byte) 0xE9, (byte) 0x18, (byte) 0x2A, (byte) 0xE0, (byte) 0x35, (byte) 0x80 };
        mStubNTAG = new StubNTAG(uid);
        mTag = new xNT(mStubNTAG.tag);

        Intent intent = new Intent();
        intent.setAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(NfcAdapter.EXTRA_TAG, mStubNTAG.tag);

        mScanActivity.onNewIntent(intent);
    }

    public void testMemberVariables() {
        assertNotNull("mTag is null", mTag);
        assertNotNull("mStubNTAG is null", mStubNTAG);
        assertNotNull("mScanActivity is null", mScanActivity);
    }
}
