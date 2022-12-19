package me.redned.simreader.storage.compression;

/**
 * Java implementation of QFS decompression <a href="https://github.com/sebamarynissen/sc4/blob/ee7bee8173ec46d155ceaa316eaab074404dff87/src/decompress.cpp">...</a>
 * <p>
 * Much of the code below contains game specific changes for SimCity 4.
 * You can find more info about the specification here: <a href="http://wiki.niotso.org/RefPack">...</a>.
 * <p>
 * Original Author: Denis Auroux
 * Version 1.22 - Copyright (C) 1998-2002
 */
public class QFS {

    public static byte[] decompress(byte[] input) {
        int packcode;

        byte[] header = new byte[5];
        System.arraycopy(input, 4, header, 0, 5);

        int length;
        int offset;

        int a, b, c;
        int inputCursor = 9; // SC4 starts at 9 always
        int outputCursor = 0;
        byte[] output = new byte[((header[2] & 0xff) << 16) + ((header[3] & 0xff) << 8) + (header[4] & 0xff)];
        while (inputCursor < input.length && (input[inputCursor] & 0xff) < 0xFC) {
            packcode = input[inputCursor] & 0xff;
            a = input[inputCursor + 1] & 0xff;
            b = input[inputCursor + 2] & 0xff;

            if ((packcode & 0x80) == 0) {
                length = packcode & 3;
                lzCompatibleMemCopy(input, inputCursor + 2, output, outputCursor, length);
                inputCursor += length + 2;
                outputCursor += length;
                length = ((packcode & 0x1c) >>> 2) + 3;
                offset = ((packcode >>> 5) << 8) + a + 1;
                lzCompatibleMemCopy(output, outputCursor - offset, output, outputCursor, length);
                outputCursor += length;
            } else if ((packcode & 0x40) == 0) {
                length = (a >>> 6) & 3;
                lzCompatibleMemCopy(input, inputCursor + 3, output, outputCursor, length);
                inputCursor += length + 3;
                outputCursor += length;
                length = (packcode & 0x3f) + 4;
                offset = (a & 0x3f) * 256 + b + 1;
                lzCompatibleMemCopy(output, outputCursor - offset, output, outputCursor, length);
                outputCursor += length;
            } else if ((packcode & 0x20) == 0) {
                c = input[inputCursor + 3] & 0xff;
                length = packcode & 3;
                lzCompatibleMemCopy(input, inputCursor + 4, output, outputCursor, length);
                inputCursor += length + 4;
                outputCursor += length;
                length = ((packcode >>> 2) & 3) * 256 + c + 5;
                offset = ((packcode & 0x10) << 12) + 256 * a + b + 1;
                lzCompatibleMemCopy(output, outputCursor - offset, output, outputCursor, length);
                outputCursor += length;
            } else {
                length = (packcode & 0x1f) * 4 + 4;
                lzCompatibleMemCopy(input, inputCursor + 1, output, outputCursor, length);
                inputCursor += length + 1;
                outputCursor += length;
            }
        }

        if ((inputCursor < output.length) && (outputCursor < output.length)) {
            lzCompatibleMemCopy(input, inputCursor + 1, output, outputCursor, input[inputCursor] & 3);
            outputCursor += input[inputCursor] & 3;
        }

        if (outputCursor != output.length) {
            System.err.println("Warning: bad length ? " + outputCursor + " instead of " + output.length);
        }

        return output;
    }

    private static void lzCompatibleMemCopy(byte[] source, int sourceOffset, byte[] destination, int destinationOffset, int length) {
        while (length-- != 0) {
            destination[destinationOffset++] = source[sourceOffset++];
        }
    }
}
