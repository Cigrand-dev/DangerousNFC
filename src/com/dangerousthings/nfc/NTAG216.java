package com.dangerousthings.nfc;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

import java.io.IOException;
import java.util.Arrays;

public class NTAG216
{
    public class BadUIDLength extends Exception {};
    public class WrongTagTechnologies extends Exception {};
    public class NotAuthenticated extends Exception {};

    private static final byte PWD_AUTH = 0x1B;
    private static final byte READ = 0x30;
    private static final byte FAST_READ = 0x3A;
    private static final byte GET_VERSION = 0x60;

    final NfcA mTag;

    public NTAG216(Tag tag) throws BadUIDLength, WrongTagTechnologies {
        if (tag.getId().length != 7) throw new BadUIDLength();
        if (!Arrays.asList(tag.getTechList()).contains("NfcA")) {
            mTag = NfcA.get(tag);
        } else throw new WrongTagTechnologies();
    }

    public void connect() throws IOException {
        mTag.connect();
    }

    public byte[] transceive(byte[] data) throws IOException {
        return mTag.transceive(data);
    }

    public byte[] authenticate(byte[] pwd) throws IOException {
        byte[] command = new byte[5];
        command[0] = PWD_AUTH;
        System.arraycopy(pwd, 0, command, 1, pwd.length);
        return transceive(command);
    }

    public byte[] read(byte addr) throws IOException, NotAuthenticated {
        byte[] response = transceive(new byte[] { READ, addr });
        if (response == null) throw new NotAuthenticated();
        return response;
    }

    public byte[] fastRead(byte startAddr, byte endAddr) throws IOException, NotAuthenticated {
        byte[] response = transceive(new byte[] { FAST_READ, startAddr, endAddr });
        if (response == null) throw new NotAuthenticated();
        return response;
    }

    public byte[] getVersion() throws IOException {
        return transceive(new byte[] { GET_VERSION });
    }
}
