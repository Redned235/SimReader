package me.redned.simreader.storage;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.DecodingFormat;
import me.redned.simreader.sc4.storage.SC4File;
import me.redned.simreader.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@AllArgsConstructor
@RequiredArgsConstructor
public class FileBuffer {
    private final byte[] buffer;

    private SC4File sc4File;
    private int cursor;

    public byte readByte() {
        return this.buffer[this.cursor++];
    }

    public int readUByte() {
        return this.buffer[this.cursor++] & 0xFF;
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(this.buffer, this.cursor, bytes, 0, length);
        this.cursor += length;
        return bytes;
    }

    public String readString(int length) {
        return new String(this.readBytes(length), StandardCharsets.UTF_8);
    }

    public boolean readBoolean() {
        try {
            return this.buffer[this.cursor] != 0;
        } finally {
            this.cursor++;
        }
    }

    public char readChar() {
        try {
            return Utils.readChar(this.cursor, this.buffer);
        } finally {
            this.cursor++;
        }
    }

    public short readUInt16() {
        try {
            return Utils.readUInt16(this.cursor, this.buffer);
        } finally {
            this.cursor += 2;
        }
    }

    public int readUInt32() {
        try {
            return Utils.readUInt32(this.cursor, this.buffer);
        } finally {
            this.cursor += 4;
        }
    }

    public int readInt32() {
        try {
            return Utils.readInt32(this.cursor, this.buffer);
        } finally {
            this.cursor += 4;
        }
    }

    public float readFloat32() {
        try {
            return Utils.readFloat32(this.cursor, this.buffer);
        } finally {
            this.cursor += 4;
        }
    }

    public long readInt64() {
        try {
            return Utils.readInt64(this.cursor, this.buffer);
        } finally {
            this.cursor += 8;
        }
    }

    public void readValueType(List<Object> data, int valueType) {
        Utils.readValueType(this, data, valueType);
    }

    public void readValueType(List<Object> data, int valueType, int strLen) {
        Utils.readValueType(this, data, valueType, strLen);
    }

    public void readValueType(Map<Integer, Utils.ValueTypeReader> dataReaders, List<Object> data, int valueType) {
        Utils.readValueType(dataReaders, this, data, valueType);
    }

    public void readValueType(Map<Integer, Utils.ValueTypeReader> dataReaders, List<Object> data, int valueType, int strLen) {
        Utils.readValueType(dataReaders, this, data, valueType, strLen);
    }

    public <T> T readAndSkip(Supplier<T> reader, int toSkip) {
        T value = reader.get();
        this.offset(toSkip);
        return value;
    }

    public <T> T readAndRun(Supplier<T> reader, Consumer<T> valueConsumer) {
        T value = reader.get();
        valueConsumer.accept(value);
        return value;
    }

    public <T> T readAndSkipIf(Supplier<T> reader, Predicate<FileBuffer> predicate, int toSkip) {
        T value = reader.get();
        if (predicate.test(this)) {
            this.offset(toSkip);
        }
        return value;
    }

    public <T> T sc4ReadAspyrSafe(Supplier<T> reader, int toSkip) {
        if (this.sc4File == null) {
            throw new IllegalArgumentException("Cannot read SC4 data without an SC4File in the buffer!");
        }

        T value = reader.get();
        if (Utils.ASYPR_EXTRA_DATA_PREDICATE.test(this)) {
            if (this.sc4File.getDecodingFormat() == DecodingFormat.WINDOWS) {
                throw new IllegalArgumentException("Got jumbled data inside SC4File! Encountered macOS data but format was set to Windows!");
            }

            this.sc4File.setDecodingFormat(DecodingFormat.MACOS);
            this.offset(toSkip);
        } else {
            if (this.sc4File.getDecodingFormat() == DecodingFormat.MACOS) {
                throw new IllegalArgumentException("Got jumbled data inside SC4File! Encountered Windows data but format was set to macOS!");
            }

            this.sc4File.setDecodingFormat(DecodingFormat.WINDOWS);
        }

        return value;
    }

    public <T> T reader(Supplier<T> reader) {
        return reader.get();
    }

    public <T> T peek(Supplier<T> reader) {
        int cursor = this.cursor;
        try {
            T value = reader.get();
            return value;
        } finally {
            this.cursor = cursor;
        }
    }

    public void offset(int amount) {
        this.cursor += amount;
    }

    public void position(int amount) {
        this.cursor = amount;
    }

    public int cursor() {
        return this.cursor;
    }

    public void resetCursor() {
        this.cursor = 0;
    }

    public int size() {
        return this.buffer.length;
    }
}
