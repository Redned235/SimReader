package me.redned.simreader.storage.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.redned.simreader.storage.FileBuffer;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class IndexEntry {
    private PersistentResourceKey resourceKey;
    private int fileLocation;
    private int fileSize;

    public static IndexEntry parse(FileBuffer buffer) {
        if (buffer.size() < 20) {
            throw new IllegalArgumentException("Buffer was not 20 bytes! Was: " + buffer.size());
        }

        return new IndexEntry(
                PersistentResourceKey.parse(buffer),
                buffer.readUInt32(),
                buffer.readUInt32()
        );
    }
}
