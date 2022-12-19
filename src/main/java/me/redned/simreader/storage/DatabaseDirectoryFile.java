package me.redned.simreader.storage;

import lombok.Getter;
import me.redned.simreader.storage.model.IndexEntry;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DatabaseDirectoryFile {
    private final IndexEntry indexEntry;

    private final int resourceCount;
    private final List<DatabaseDirectoryResource> resources = new ArrayList<>();

    public DatabaseDirectoryFile(IndexEntry indexEntry) {
        this.indexEntry = indexEntry;

        this.resourceCount = indexEntry.getFileSize() / 16;
    }

    public void addResource(DatabaseDirectoryResource resource) {
        this.resources.add(resource);
    }
}
