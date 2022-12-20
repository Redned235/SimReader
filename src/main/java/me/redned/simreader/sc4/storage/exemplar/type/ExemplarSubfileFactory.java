package me.redned.simreader.sc4.storage.exemplar.type;

import java.util.List;

public interface ExemplarSubfileFactory<T extends ExemplarSubfile> {

    T create(String fileIdentifier, ExemplarSubfile.Format format, int versionNumber, int type, int group, int instance, List<ExemplarSubfile.Property> properties);
}
