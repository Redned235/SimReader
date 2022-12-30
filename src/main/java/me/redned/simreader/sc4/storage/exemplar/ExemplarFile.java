package me.redned.simreader.sc4.storage.exemplar;

import lombok.Getter;
import me.redned.simreader.sc4.SC4ResourceTypes;
import me.redned.simreader.sc4.storage.exemplar.type.CohortSubfile;
import me.redned.simreader.sc4.storage.exemplar.type.ExemplarSubfile;
import me.redned.simreader.storage.DatabasePackedFile;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.IndexEntry;
import me.redned.simreader.storage.model.PersistentResourceKey;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DatabasePackedFile} for SimCity 4 that contains
 * Exemplar data for the game.
 */
@Getter
public class ExemplarFile extends DatabasePackedFile {
    private final Map<PersistentResourceKey, ExemplarSubfile> exemplarFiles = new HashMap<>();
    private final Map<PersistentResourceKey, CohortSubfile> cohortFiles = new HashMap<>();

    public ExemplarFile(Path path) throws IOException {
        super(path);

        this.read();
    }

    protected void read() throws IOException {
        super.read();

        for (Map.Entry<PersistentResourceKey, IndexEntry> entry : this.indexEntries.entrySet()) {
            if (entry.getKey().getType() == SC4ResourceTypes.EXEMPLAR_SUBFILE) {
                byte[] exemplarData = this.getBytesAtIndex(entry.getValue());

                this.exemplarFiles.put(entry.getKey(), ExemplarSubfile.parse(ExemplarSubfile::new, new FileBuffer(exemplarData)));
            }

            if (entry.getKey().getType() == SC4ResourceTypes.COHORT_SUBFILE) {
                byte[] exemplarData = this.getBytesAtIndex(entry.getValue());

                this.cohortFiles.put(entry.getKey(), ExemplarSubfile.parse(CohortSubfile::new, new FileBuffer(exemplarData)));
            }
        }

        this.buildCohortTree();
    }

    private void buildCohortTree() {
        // Add exemplar files to cohorts
        for (Map.Entry<PersistentResourceKey, ExemplarSubfile> entry : this.exemplarFiles.entrySet()) {
            PersistentResourceKey cohortKey = entry.getValue().getCohortResourceKey();
            if (cohortKey.isEmpty()) {
                continue;
            }

            CohortSubfile cohort = this.cohortFiles.get(cohortKey);
            cohort.addChild(entry.getKey(), entry.getValue());
        }

        // Add nested cohorts
        for (Map.Entry<PersistentResourceKey, CohortSubfile> entry : this.cohortFiles.entrySet()) {
            PersistentResourceKey cohortKey = entry.getValue().getCohortResourceKey();
            if (cohortKey.isEmpty()) {
                continue;
            }

            CohortSubfile cohort = this.cohortFiles.get(cohortKey);
            cohort.addChild(entry.getKey(), entry.getValue());
        }
    }

    public List<ExemplarSubfile> getExemplarFiles(int type) {
        List<ExemplarSubfile> files = new ArrayList<>();
        for (Map.Entry<PersistentResourceKey, ExemplarSubfile> entry : this.exemplarFiles.entrySet()) {
            if (entry.getKey().getType() == type) {
                files.add(entry.getValue());
            }
        }

        return files;
    }
}
