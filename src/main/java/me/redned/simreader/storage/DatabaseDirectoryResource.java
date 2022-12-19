package me.redned.simreader.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.redned.simreader.storage.model.PersistentResourceKey;

@Getter
@AllArgsConstructor
public class DatabaseDirectoryResource {
    private final PersistentResourceKey resourceKey;
    private final int decompressedSize;

    public static DatabaseDirectoryResource parse(FileBuffer buffer) {
        if (buffer.size() < 16) {
            throw new IllegalArgumentException("Buffer was not 16 bytes! Was: " + buffer.size());
        }

        return new DatabaseDirectoryResource(PersistentResourceKey.parse(buffer), buffer.readUInt32());
    }
}
