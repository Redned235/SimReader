package me.redned.simreader.util;

import me.redned.simreader.storage.FileBuffer;

import java.util.List;
import java.util.Map;

public class Utils {
    public static final Map<Integer, ValueTypeReader> SGPROP_DATA_READERS = Map.of(
            0x01, (data, buffer, len) -> data.add(buffer.readByte()),
            0x02, (data, buffer, len) ->  data.add(buffer.readUInt16()),
            0x03, (data, buffer, len) ->  data.add(buffer.readUInt32()),
            0x07, (data, buffer, len) ->  data.add(buffer.readInt32()),
            0x08, (data, buffer, len) ->  data.add(buffer.readInt64()),
            0x09, (data, buffer, len) ->  data.add(buffer.readFloat32()),
            0x0B, (data, buffer, len) ->  data.add(buffer.readBoolean()),
            0x0C, (data, buffer, len) ->  data.add(buffer.readString(len))
    );

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

    public static float readFloat32(int index, byte[] bytes) {
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

    public static void readValueType(FileBuffer buffer, List<Object> data, int valueType) {
        readValueType(buffer, data, valueType, 1);
    }

    public static void readValueType(FileBuffer buffer, List<Object> data, int valueType, int strLen) {
        readValueType(SGPROP_DATA_READERS, buffer, data, valueType, strLen);
    }

    public static void readValueType(Map<Integer, ValueTypeReader> dataReaders, FileBuffer buffer, List<Object> data, int valueType) {
        readValueType(dataReaders, buffer, data, valueType, 1);
    }

    public static void readValueType(Map<Integer, ValueTypeReader> dataReaders, FileBuffer buffer, List<Object> data, int valueType, int strLen) {
        ValueTypeReader reader = dataReaders.get(valueType);
        if (reader == null) {
            return;
        }

        reader.read(data, buffer, strLen);
    }

    public interface ValueTypeReader {

        void read(List<Object> data, FileBuffer buffer, int len);
    }
}
