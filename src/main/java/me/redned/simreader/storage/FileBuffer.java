package me.redned.simreader.storage;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.Utils;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@RequiredArgsConstructor
public class FileBuffer {
    private final byte[] buffer;

    private int cursor;

    public byte readByte() {
        return this.buffer[this.cursor++];
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

    public float readSingle() {
        try {
            return Utils.readSingle(this.cursor, this.buffer);
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

    public void skip(int amount) {
        this.cursor += amount;
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
