package com.dangerousthings.nfc;

import android.nfc.ErrorCodes;
import android.nfc.FormatException;
import android.nfc.INfcTag;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.nfc.TransceiveResult;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

public class StubNTAG implements INfcTag {
    public final Tag tag;
    private static final int[] techList = { TagTechnology.NFC_A };

    public StubNTAG(byte[] id) {
        Bundle[] techListExtras = { new Bundle() };
        tag = new Tag(id, techList, techListExtras, 0, this);
    }

    public IBinder asBinder() {
        return null;
    }

    public int close(int nativeHandle) {
        return ErrorCodes.SUCCESS;
    }

    public int connect(int nativeHandle, int technology) {
        return ErrorCodes.SUCCESS;
    }

    public int reconnect(int nativeHandle) {
        return ErrorCodes.SUCCESS;
    }

    public int[] getTechList(int nativeHandle) {
        return techList;
    }

    public boolean isNdef(int nativeHandle) {
        return false;
    }

    public boolean isPresent(int nativeHandle) {
        return true;
    }

    public TransceiveResult transceive(int nativeHandle, byte[] data, boolean raw) {
        return new TransceiveResult(0, null);
    }

    public NdefMessage ndefRead(int nativeHandle) {
        return null;
    }

    public int ndefWrite(int nativeHandle, NdefMessage msg) {
        return ErrorCodes.SUCCESS;
    }

    public int ndefMakeReadOnly(int nativeHandle) {
        return ErrorCodes.SUCCESS;
    }

    public boolean ndefIsWritable(int nativeHandle) {
        return true;
    }

    public int formatNdef(int nativeHandle, byte[] key) {
        return ErrorCodes.SUCCESS;
    }

    public Tag rediscover(int nativehandle) {
        return tag;
    }

    public int setTimeout(int technology, int timeout) {
        return ErrorCodes.SUCCESS;
    }

    public int getTimeout(int technology) {
        return ErrorCodes.SUCCESS;
    }

    public void resetTimeouts() {
    }

    public boolean canMakeReadOnly(int ndefType) {
        return false;
    }

    public int getMaxTransceiveLength(int technology) {
        return 32;
    }

    public boolean getExtendedLengthApdusSupported() {
        return false;
    }
}
