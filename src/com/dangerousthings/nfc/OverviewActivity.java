package com.dangerousthings.nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import android.app.AlertDialog;

import java.io.IOException;
import java.util.Arrays;

public class OverviewActivity extends Activity
{
    byte[] mUID = null;
    byte[] mPassword = null;
    byte[] mAllPages = null;
    byte[] mPage02 = null;
    byte[] mPage03 = null;
    byte[] mPageE2 = null;
    byte[] mPageE3 = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        mUID = getIntent().getByteArrayExtra(ScanActivity.UID);
        mPassword = getIntent().getByteArrayExtra(ScanActivity.PASSWORD);
        mAllPages = getIntent().getByteArrayExtra(ScanActivity.ALL_PAGES);

        mPage02 = getIntent().getByteArrayExtra(ScanActivity.PAGE_02);
        if (mPage02 == null) mPage02 = Arrays.copyOfRange(mAllPages, 4*0x02, 4*0x02+4);
        mPage03 = getIntent().getByteArrayExtra(ScanActivity.PAGE_03);
        if (mPage03 == null) mPage03 = Arrays.copyOfRange(mAllPages, 4*0x03, 4*0x03+4);
        mPageE2 = getIntent().getByteArrayExtra(ScanActivity.PAGE_E2);
        if (mPageE2 == null) mPageE2 = Arrays.copyOfRange(mAllPages, 4*0xE2, 4*0xE2+4);
        mPageE3 = getIntent().getByteArrayExtra(ScanActivity.PAGE_E3);
        if (mPageE3 == null) mPageE3 = Arrays.copyOfRange(mAllPages, 4*0xE3, 4*0xE3+4);

        ((TextView) findViewById(R.id.uid)).setText(HexUtils.bytesToHex(mUID));
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Okay", null)
            .show();
    }
}
