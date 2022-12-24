package me.redned.simreader.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.storage.compression.QFS;
import me.redned.simreader.storage.model.IndexEntry;
import me.redned.simreader.storage.model.PersistentResourceKey;
import me.redned.simreader.util.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class DatabasePackedFile {
    protected final Path path;
    protected final Map<PersistentResourceKey, IndexEntry> indexEntries = new HashMap<>();
    protected final Map<Integer, IndexEntry> entryByTypeCache = new WeakHashMap<>();

    @Getter
    protected DatabaseDirectoryFile directoryFile;

    public void read() throws IOException {
        try (FileInputStream stream = new FileInputStream(this.path.toFile())) {

            byte[] headerBytes = new byte[100];
            stream.read(headerBytes, 0, 96);

            Header header = new Header(new FileBuffer(headerBytes));

            stream.getChannel().position(header.firstIndexOffset);

            for (int i = 0; i < header.indexCount; i++) {
                byte[] indexBytes = new byte[20];

                stream.read(indexBytes, 0, 20);
                IndexEntry entry = IndexEntry.parse(new FileBuffer(indexBytes));
                this.indexEntries.put(entry.getResourceKey(), entry);
            }

            IndexEntry dbdfEntry = this.indexEntries.get(ResourceKeys.DBDF);
            if (dbdfEntry == null) {
                throw new IllegalArgumentException("Could not find Database Directory file!");
            }

            DatabaseDirectoryFile directoryFile = new DatabaseDirectoryFile(dbdfEntry);

            stream.getChannel().position(dbdfEntry.getFileLocation());
            for (int i = 0; i < directoryFile.getResourceCount(); i++) {
                byte[] resourceBytes = new byte[16];
                stream.read(resourceBytes, 0, 16);

                DatabaseDirectoryResource resource = DatabaseDirectoryResource.parse(new FileBuffer(resourceBytes));
                directoryFile.addResource(resource);
            }

            this.directoryFile = directoryFile;
        }
    }

    public IndexEntry getEntry(PersistentResourceKey key) {
        return this.indexEntries.get(key);
    }

    public IndexEntry getEntryFromType(int type) {
        return this.entryByTypeCache.computeIfAbsent(type, e -> {
            for (Map.Entry<PersistentResourceKey, IndexEntry> entry : this.indexEntries.entrySet()) {
                if (entry.getKey().getType() == type) {
                    return entry.getValue();
                }
            }

            return null;
        });
    }

    public byte[] getBytesAtIndex(IndexEntry entry) throws IOException {
        byte[] compressedBytes = this.getRawBytesAtIndex(entry);
        boolean compressed = ((compressedBytes[4] & 0xff) << 8) + (compressedBytes[5] & 0xff) == QFS.COMPRESSION_FLAG;
        if (compressed) {
            return QFS.decompress(compressedBytes);
        }

        return compressedBytes;
    }

    protected boolean isCompressed(IndexEntry entry) {
        return this.indexEntries.containsKey(entry.getResourceKey());
    }

    protected byte[] getRawBytesAtIndex(IndexEntry entry) throws IOException {
        try (FileInputStream stream = new FileInputStream(this.path.toFile())) {
            int fileSize = entry.getFileSize();

            stream.getChannel().position(entry.getFileLocation());

            byte[] bytes = new byte[fileSize];
            stream.read(bytes, 0, fileSize);
            return bytes;
        }
    }

    public static class Header {
        private final String identifier;
        private final int majorVersion;
        private final int minorVersion;
        private final Instant dateCreated;
        private final Instant lastModified;

        private final int indexMajorVersion;
        private final int indexCount;
        private final int firstIndexOffset;
        private final int indexSize;
        private final int holeCount;
        private final int holeOffset;
        private final int holeSize;
        private final int indexMinorVersion;

        public Header(FileBuffer buffer) {
            if (buffer.size() < 96) {
                throw new IllegalArgumentException("Buffer was not 96 bytes! Was: " + buffer.size());
            }

            this.identifier = buffer.readString(4);
            this.majorVersion = buffer.readUInt32();
            this.minorVersion = buffer.readUInt32();

            buffer.skip(12);

            this.dateCreated = Instant.ofEpochMilli(buffer.readUInt32());
            this.lastModified = Instant.ofEpochMilli(buffer.readUInt32());
            this.indexMajorVersion = buffer.readUInt32();
            this.indexCount = buffer.readUInt32();
            this.firstIndexOffset = buffer.readUInt32();
            this.indexSize = buffer.readUInt32();
            this.holeCount = buffer.readUInt32();
            this.holeOffset = buffer.readUInt32();
            this.holeSize = buffer.readUInt32();
            this.indexMinorVersion = buffer.readUInt32();
        }

        @Override
        public String toString() {
            return "Header{" +
                    "identifier='" + identifier + '\'' +
                    ", majorVersion=" + majorVersion +
                    ", minorVersion=" + minorVersion +
                    ", dateCreated=" + dateCreated +
                    ", lastModified=" + lastModified +
                    ", indexMajorVersion=" + indexMajorVersion +
                    ", indexCount=" + indexCount +
                    ", firstIndexOffset=" + firstIndexOffset +
                    ", indexSize=" + indexSize +
                    ", holeCount=" + holeCount +
                    ", holeOffset=" + holeOffset +
                    ", holeSize=" + holeSize +
                    ", indexMinorVersion=" + indexMinorVersion +
                    '}';
        }
    }
}
