package com.dangerousthings.nfc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import java.nio.charset.Charset;
import java.util.Arrays;

public class PasswordFragment extends DialogFragment {
    public interface OnPasswordListener {
        public void onPasswordInput(byte[] password);
    }

    class BytesLengthFilter implements InputFilter {
        Charset mCharset;
        int mBytesLengthLimit;

        BytesLengthFilter(Charset charset, int bytesLengthLimit) {
            mCharset = charset;
            mBytesLengthLimit = bytesLengthLimit;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int length = dest.toString().getBytes(mCharset).length + source.toString().getBytes(mCharset).length;
            return (length > mBytesLengthLimit) ? "" : null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.password, null);

        final EditText password = (EditText) view.findViewById(R.id.password);
        password.setFilters(new InputFilter[]{ new BytesLengthFilter(Charset.forName("UTF-8"), 4) });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
               .setMessage("Password?")
               .setNegativeButton("Cancel", null)
               .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       byte[] passwordValue = Arrays.copyOf(password.getText().toString().getBytes(Charset.forName("UTF-8")), 4);
                       ((OnPasswordListener) getActivity()).onPasswordInput(passwordValue);
                   }
               });
        return builder.create();
    }
}
