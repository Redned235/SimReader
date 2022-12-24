package me.redned.simreader.sc4.storage.exemplar.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;
import me.redned.simreader.storage.model.PersistentResourceKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CohortSubfile extends ExemplarSubfile {
    private final Map<PersistentResourceKey, ExemplarSubfile> children = new HashMap<>();

    public CohortSubfile(String fileIdentifier, Format format, int versionNumber, int type, int group, int instance, Map<Integer, ExemplarProperty<?, ?>> properties) {
        super(fileIdentifier, format, versionNumber, type, group, instance, properties);
    }

    public void addChild(PersistentResourceKey key, ExemplarSubfile subfile) {
        this.children.put(key, subfile);

        subfile.setCohort(this);
    }

    public Map<PersistentResourceKey, ExemplarSubfile> getChildren() {
        return Collections.unmodifiableMap(this.children);
    }
}
