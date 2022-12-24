package me.redned.simreader.sc4.storage.exemplar.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;

import java.util.Map;

public interface ExemplarSubfileFactory<T extends ExemplarSubfile> {

    T create(String fileIdentifier, ExemplarSubfile.Format format, int versionNumber, int type, int group, int instance, Map<Integer, ExemplarProperty<?, ?>> properties);
}
