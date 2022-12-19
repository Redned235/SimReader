package me.redned.simreader.sc4.storage;

import lombok.Getter;
import me.redned.simreader.sc4.ResourceTypes;
import me.redned.simreader.sc4.SC4ResourceKeys;
import me.redned.simreader.sc4.storage.type.BuildingFile;
import me.redned.simreader.sc4.storage.type.LotFile;
import me.redned.simreader.sc4.storage.type.RegionViewFile;
import me.redned.simreader.storage.DatabasePackedFile;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.IndexEntry;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A {@link DatabasePackedFile} for Sim City 4.
 */
@Getter
public class SC4File extends DatabasePackedFile {
    private LotFile lotFile;
    private BuildingFile buildingFile;
    private RegionViewFile regionViewFile;

    public SC4File(Path path) {
        super(path);
    }

    @Override
    public void read() throws IOException {
        super.read();

        this.readLots();
        this.readBuildings();
        this.readRegionView();
    }

    private void readLots() throws IOException {
        IndexEntry entry = this.getEntryFromType(ResourceTypes.LOT);
        if (entry == null) {
            System.out.println("No lots");
            return;
        }

        byte[] lotFileData = this.getBytesAtIndex(entry);
        this.lotFile = LotFile.parse(lotFileData, lotFileData.length);

        System.out.println("Read " + this.lotFile.getLots().size() + " lots!");
    }

    private void readBuildings() throws IOException {
        IndexEntry entry = this.getEntryFromType(ResourceTypes.BUILDING);
        if (entry == null) {
            System.out.println("No buildings");
            return;
        }

        byte[] buildingFileData = this.getBytesAtIndex(entry);
        this.buildingFile = BuildingFile.parse(buildingFileData, buildingFileData.length);

        System.out.println("Read " + this.buildingFile.getBuildings().size() + " buildings!");
    }

    private void readRegionView() throws IOException {
        IndexEntry entry = this.getEntry(SC4ResourceKeys.REGION_VIEW);
        if (entry == null) {
            System.out.println("No region info");
            return;
        }

        byte[] regionViewFileData = this.getBytesAtIndex(entry);
        this.regionViewFile = RegionViewFile.parse(new FileBuffer(regionViewFileData));
    }
}
