package me.redned.simreader.storage;

import lombok.Getter;
import me.redned.simreader.storage.compression.QFS;
import me.redned.simreader.storage.model.IndexEntry;
import me.redned.simreader.storage.model.PersistentResourceKey;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class DatabasePackedFile {
    protected final Path path;

    @Getter
    protected final Map<PersistentResourceKey, IndexEntry> indexEntries = new HashMap<>();
    protected final Map<Integer, IndexEntry> entryByTypeCache = new WeakHashMap<>();

    @Getter
    protected DatabaseDirectoryFile directoryFile;

    public DatabasePackedFile(Path path) {
        this.path = path;
    }

    protected void read() throws IOException {
        try (FileChannel channel = FileChannel.open(this.path)) {
            ByteBuffer headerBytes = ByteBuffer.allocate(96);
            channel.read(headerBytes);

            Header header = new Header(new FileBuffer(headerBytes.array()));

            channel.position(header.firstIndexOffset);

            for (int i = 0; i < header.indexCount; i++) {
                ByteBuffer indexBytes = ByteBuffer.allocate(20);

                channel.read(indexBytes);
                IndexEntry entry = IndexEntry.parse(new FileBuffer(indexBytes.array()));
                this.indexEntries.put(entry.getResourceKey(), entry);
            }

            IndexEntry dbdfEntry = this.indexEntries.get(ResourceKeys.DBDF);
            if (dbdfEntry == null) {
                throw new IllegalArgumentException("Could not find Database Directory file!");
            }

            DatabaseDirectoryFile directoryFile = new DatabaseDirectoryFile(dbdfEntry);

            channel.position(dbdfEntry.getFileLocation());
            for (int i = 0; i < directoryFile.getResourceCount(); i++) {
                ByteBuffer resourceBytes = ByteBuffer.allocate(16);
                channel.read(resourceBytes);

                DatabaseDirectoryResource resource = DatabaseDirectoryResource.parse(new FileBuffer(resourceBytes.array()));
                directoryFile.addResource(resource);
            }

            this.directoryFile = directoryFile;
        }
    }

    public IndexEntry getEntry(PersistentResourceKey key) {
        return this.indexEntries.get(key);
    }

    public IndexEntry getFirstEntryFromType(int type) {
        return this.entryByTypeCache.computeIfAbsent(type, e -> {
            for (Map.Entry<PersistentResourceKey, IndexEntry> entry : this.indexEntries.entrySet()) {
                if (entry.getKey().getType() == type) {
                    return entry.getValue();
                }
            }

            return null;
        });
    }

    public List<IndexEntry> getEntriesFromType(int type) {
        List<IndexEntry> entries = new ArrayList<>();
        for (Map.Entry<PersistentResourceKey, IndexEntry> entry : this.indexEntries.entrySet()) {
            if (entry.getValue().getResourceKey().getType() == type) {
                entries.add(entry.getValue());
            }
        }

        return entries;
    }

    public byte[] getBytesAtIndex(IndexEntry entry) throws IOException {
        byte[] compressedBytes = this.getRawBytesAtIndex(entry);
        boolean compressed = ((compressedBytes[4] & 0xff) << 8) + (compressedBytes[5] & 0xff) == QFS.COMPRESSION_FLAG;
        if (compressed) {
            return QFS.decompress(compressedBytes);
        }

        return compressedBytes;
    }

    protected byte[] getRawBytesAtIndex(IndexEntry entry) throws IOException {
        try (FileChannel channel = FileChannel.open(this.path)) {
            int fileSize = entry.getFileSize();

            channel.position(entry.getFileLocation());

            ByteBuffer bytes = ByteBuffer.allocate(fileSize);
            channel.read(bytes);
            return bytes.array();
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

            buffer.offset(12);

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
