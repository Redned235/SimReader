package me.redned.simreader;

public class Utils {

    public static int readUInt32(int index, byte[] bytes) {
        long result = (int) bytes[index] & 0xff;
        result |= ((int) bytes[index + 1] & 0xff) << 8;
        result |= ((int) bytes[index + 2] & 0xff) << 16;
        result |= (long) ((int) bytes[index + 3] & 0xff) << 24;
        return (int) (result & 0xFFFFFFFFL);
    }

    public static short readUInt16(int index, byte[] bytes) {
        return (short) (((bytes[index + 1] & 0xFF) << 8) | (bytes[index] & 0xFF));
    }

    public static float readSingle(int index, byte[] bytes) {
        return Float.intBitsToFloat(readInt32(index, bytes));
    }

    public static int readInt32(int index, byte[] bytes) {
        return (0xff & bytes[index]) << 56 | (0xff & bytes[index + 1]) << 48
                | (0xff & bytes[index + 2]) << 40 | (0xff & bytes[index + 3]) << 32;
    }

    public static long readInt64(int index, byte[] bytes) {
        return ((long) (0xff & bytes[index]) << 56 | (long) (0xff & bytes[index + 1]) << 48
                | (long) (0xff & bytes[index + 2]) << 40 | (long) (0xff & bytes[index + 3]) << 32
                | (long) (0xff & bytes[index + 4]) << 24 | (long) (0xff & bytes[index + 5]) << 16
                | (long) (0xff & bytes[index + 6]) << 8 | (long) (0xff & bytes[index + 7]) << 0);
    }

    public static char readChar(int index, byte[] bytes) {
        return (char) ((0xff & bytes[index]) << 8 | (0xff & bytes[index + 1]) << 0);
    }
}
