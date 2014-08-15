package com.dangerousthings.nfc;

import com.dangerousthings.nearfield.tech.Ntag216;
import com.dangerousthings.nearfield.utils.OtpUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends Activity implements PasswordFragment.OnPasswordListener
{
    static byte NEW_AUTH0 = (byte)0xE2;
    static byte[] NEW_CC = new byte[] { (byte)0xE1, (byte)0x12, (byte)0x6D, (byte)0x00 };
    static byte[] NEW_PACK = new byte[] { (byte)0x44, (byte)0x54 };
    static byte[] NEW_STATIC_LOCK_BYTES = new byte[] { (byte)0x0F, (byte)0x00 };
    static byte[] NEW_DYNAMIC_LOCK_BYTES = new byte[] { (byte)0x00, (byte)0x00, (byte)0x7F };

    byte[] mPassword = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        super.onResume();

        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null) adapter.enableForegroundDispatch(this, intent, null, null);
    }

    public void onNewIntent(Intent intent) {
        if (mPassword != null) {
            byte[] password = new byte[4];
            System.arraycopy(mPassword, 0, password, 0, mPassword.length);

            Tag intentTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            try {
                Ntag216 tag = Ntag216.get(intentTag);
                tag.connect();

                boolean badVersion = !Arrays.equals(Ntag216.VERSION, tag.getVersion());
                if (badVersion) throw new IOException("Error: Unsupported Tag");

                // tag.pwdAuth(password);

                boolean alteredStaticLockBytes = !Arrays.equals(Ntag216.DEFAULT_STATIC_LOCK_BYTES, tag.getStaticLockBytes());
                boolean alteredDynamicLockBytes = !Arrays.equals(Ntag216.DEFAULT_DYNAMIC_LOCK_BYTES, tag.getDynamicLockBytes());
                if (alteredStaticLockBytes || alteredDynamicLockBytes) throw new IOException("Error: Lock Bits Already Altered");

                byte[] currentCC = tag.getCC();
                if (OtpUtils.isWritePossible(currentCC, NEW_CC)) {
                    tag.setCC(NEW_CC);
                } else if ((currentCC[0] != (byte)0xE1) || (currentCC[1] != (byte)0x11) ||
                           (currentCC[2] != (byte)0x6D) || (currentCC[3] != (byte)0x00)) {
                    throw new IOException("Error: Bad Capability Container");
                }

                tag.setStaticLockBytes(NEW_STATIC_LOCK_BYTES);
                tag.setDynamicLockBytes(NEW_DYNAMIC_LOCK_BYTES);

                tag.setPass(password);
                tag.setPack(NEW_PACK);
                tag.setAuth0(NEW_AUTH0);

                showAlert("Success! Tag has been updated! :)");
            } catch (IOException e) {
                showAlert(e.getMessage());
            }
        }
    }

    public void requestPassword(View view) {
        PasswordFragment.newInstance(mPassword).show(getFragmentManager(), null);
    }

    public void onPasswordInput(byte[] password) {
        setPasswordBytes(password);
    }

    public byte[] getPasswordBytes() {
        return mPassword;
    }

    public void setPasswordBytes(byte[] passwordBytes) {
        mPassword = passwordBytes;

        runOnUiThread(new Runnable() {
            public void run() {
                if (mPassword != null) {
                    findViewById(R.id.scan_tag_now).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.scan_tag_now).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Okay", null)
            .show();
    }
}
