package com.dangerousthings.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;

import android.app.AlertDialog;

import java.io.IOException;
import java.util.Arrays;

public class ScanActivity extends Activity implements PasswordFragment.OnPasswordListener
{
    public final static String UID = "com.dangerousthings.nfc.UID";
    public final static String PASSWORD = "com.dangerousthings.nfc.PASSWORD";
    public final static String ALL_PAGES = "com.dangerousthings.nfc.ALL_PAGES";
    public final static String PAGE_02 = "com.dangerousthings.nfc.PAGE_02";
    public final static String PAGE_03 = "com.dangerousthings.nfc.PAGE_03";
    public final static String PAGE_E2 = "com.dangerousthings.nfc.PAGE_E2";
    public final static String PAGE_E3 = "com.dangerousthings.nfc.PAGE_E3";

    xNT mTag;

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
        setContentView(R.layout.scan);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, intent, null, null);
    }

    public void onNewIntent(Intent intent) {
        Tag intentTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (!Arrays.equals(mUID, intentTag.getId())) {
            mUID = intentTag.getId();
            mPassword = null;
            mAllPages = null;
            mPage02 = null; mPage03 = null; mPageE2 = null; mPageE3 = null;
        }

        try {
            mTag = new xNT(intentTag);
            mTag.connect();
            mTag.checkVersion();

            if (mPassword != null) {
                mTag.authenticate(mPassword);
            }

            try {
                mAllPages = mTag.readAllPages();
                launchOverview();
            } catch (xNT.NotAuthenticated eFast) {
                try {
                    mPage02 = mTag.read((byte) 0x02);
                    mPage03 = mTag.read((byte) 0x03);
                    mPageE2 = mTag.read((byte) 0xE2);
                    mPageE3 = mTag.read((byte) 0xE3);
                    launchOverview();
                } catch (xNT.NotAuthenticated ePiecemeal) {
                    requestPassword();
                }
            }
        } catch (xNT.BadUIDLength e) {
            showAlert("Tag Not Supported");
        } catch (xNT.WrongTagTechnologies e) {
            showAlert("Tag Not Supported");
        } catch (xNT.BadTagVersion e) {
            showAlert("Tag Not Supported");
        } catch (IOException e) {
            showAlert(e.getMessage());
        }
    }

    public void requestPassword() {
        new PasswordFragment().show(getFragmentManager(), null);
    }

    public void onPasswordInput(byte[] password) {
        mPassword = password;
    }

    public void launchOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra(UID, mUID);

        if (mPassword != null) intent.putExtra(PASSWORD, mPassword);

        if (mAllPages != null) {
            intent.putExtra(ALL_PAGES, mAllPages);
        } else {
            if (mPage02 != null) intent.putExtra(PAGE_02, mPage02);
            if (mPage03 != null) intent.putExtra(PAGE_03, mPage03);
            if (mPageE2 != null) intent.putExtra(PAGE_E2, mPageE2);
            if (mPageE3 != null) intent.putExtra(PAGE_E3, mPageE3);
        }

        startActivity(intent);
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Okay", null)
            .show();
    }
}
