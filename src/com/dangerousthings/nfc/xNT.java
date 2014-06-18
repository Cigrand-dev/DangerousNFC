package com.dangerousthings.nfc;

import android.nfc.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Arrays;

public class xNT extends NTAG216
{
    public class BadTagVersion extends Exception {};

    private static final byte[] NTAG216_VERSION = { 0x00, 0x04, 0x04, 0x02, 0x01, 0x00, 0x13, 0x03 };

    public xNT(Tag tag) throws WrongTagTechnologies {
        super(tag);
    }

    public void checkVersion() throws IOException, BadTagVersion {
        if (!Arrays.equals(getVersion(), NTAG216_VERSION)) throw new BadTagVersion();
    }

    public byte[] readAllPages() throws IOException, NotAuthenticated {
        ByteArrayOutputStream data = new ByteArrayOutputStream();

        data.write(fastRead((byte) 0x00, (byte) 0x3E), 0, 252);
        data.write(fastRead((byte) 0x3F, (byte) 0x7D), 0, 252);
        data.write(fastRead((byte) 0x7E, (byte) 0xBC), 0, 252);
        data.write(fastRead((byte) 0xBD, (byte) 0xE4), 0, 160);

        return data.toByteArray();
    }
}
