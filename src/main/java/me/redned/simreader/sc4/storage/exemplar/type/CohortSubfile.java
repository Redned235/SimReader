package me.redned.simreader.sc4.storage.exemplar.type;

import me.redned.simreader.storage.model.PersistentResourceKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohortSubfile extends ExemplarSubfile {
    private final Map<PersistentResourceKey, ExemplarSubfile> children = new HashMap<>();

    public CohortSubfile(String fileIdentifier, Format format, int versionNumber, int type, int group, int instance, List<Property> properties) {
        super(fileIdentifier, format, versionNumber, type, group, instance, properties);
    }

    public void addChild(PersistentResourceKey key, ExemplarSubfile subfile) {
        this.children.put(key, subfile);
    }

    public Map<PersistentResourceKey, ExemplarSubfile> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }
}
