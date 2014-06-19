package com.dangerousthings.nfc;

import java.util.InputMismatchException;

public class OTPUtils
{
    public static boolean isWritePossible(byte[] from, byte[] to) throws InputMismatchException {
        if (from.length != to.length) throw new InputMismatchException();
        for (int i = 0; i < from.length; i++) {
            if ((from[i] | to[i]) != to[i]) return false;
        }
        return true;
    }
}
