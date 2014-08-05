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

public class MainActivity extends Activity implements PasswordFragment.OnPasswordListener
{
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
        Tag intentTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        try {
            xNT tag = new xNT(intentTag);
            tag.connect();
            tag.checkVersion();

            byte[] page02 = tag.read((byte) 0x02);
            byte[] page03 = tag.read((byte) 0x03);
            byte[] pageE2 = tag.read((byte) 0xE2);
            byte[] pageE3 = tag.read((byte) 0xE3);
        } catch (xNT.BadUIDLength e) {
            showAlert("Error: Tag Not Supported");
        } catch (xNT.WrongTagTechnologies e) {
            showAlert("Error: Tag Not Supported");
        } catch (xNT.BadTagVersion e) {
            showAlert("Error: Tag Not Supported");
        } catch (xNT.NotAuthenticated e) {
            showAlert("Error: Password Already Set");
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

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Okay", null)
            .show();
    }
}
