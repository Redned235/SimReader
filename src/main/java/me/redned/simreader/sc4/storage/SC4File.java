package me.redned.simreader.sc4.storage;

import lombok.Getter;
import me.redned.simreader.sc4.SC4ResourceKeys;
import me.redned.simreader.sc4.SC4ResourceTypes;
import me.redned.simreader.sc4.storage.type.BuildingSubfile;
import me.redned.simreader.sc4.storage.type.FloraSubfile;
import me.redned.simreader.sc4.storage.type.LotSubfile;
import me.redned.simreader.sc4.storage.type.PropSubfile;
import me.redned.simreader.sc4.storage.type.RegionViewSubfile;
import me.redned.simreader.sc4.storage.type.TerrainSubfile;
import me.redned.simreader.storage.DatabasePackedFile;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.IndexEntry;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A {@link DatabasePackedFile} for SimCity 4.
 */
@Getter
public class SC4File extends DatabasePackedFile {
    private LotSubfile lotFile;
    private BuildingSubfile buildingFile;
    private PropSubfile propFile;
    private FloraSubfile floraFile;
    private RegionViewSubfile regionViewFile;
    private TerrainSubfile terrainFile;

    public SC4File(Path path) {
        super(path);
    }

    @Override
    public void read() throws IOException {
        super.read();

        this.readLots();
        this.readBuildings();
        this.readProps();
        this.readFlora();
        this.readRegionView();
        this.readTerrain();
    }

    private void readLots() throws IOException {
        IndexEntry entry = this.getEntryFromType(SC4ResourceTypes.LOT_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] lotFileData = this.getBytesAtIndex(entry);
        this.lotFile = LotSubfile.parse(lotFileData, lotFileData.length);
    }

    private void readBuildings() throws IOException {
        IndexEntry entry = this.getEntryFromType(SC4ResourceTypes.BUILDING_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] buildingFileData = this.getBytesAtIndex(entry);
        this.buildingFile = BuildingSubfile.parse(buildingFileData, buildingFileData.length);
    }

    private void readProps() throws IOException {
        IndexEntry entry = this.getEntryFromType(SC4ResourceTypes.PROP_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] propFileData = this.getBytesAtIndex(entry);
        this.propFile = PropSubfile.parse(propFileData, propFileData.length);
    }

    private void readFlora() throws IOException {
        IndexEntry entry = this.getEntryFromType(SC4ResourceTypes.FLORA_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] floraFileData = this.getBytesAtIndex(entry);
        this.floraFile = FloraSubfile.parse(floraFileData, floraFileData.length);
    }

    private void readRegionView() throws IOException {
        IndexEntry entry = this.getEntry(SC4ResourceKeys.REGION_VIEW);
        if (entry == null) {
            return;
        }

        byte[] regionViewFileData = this.getBytesAtIndex(entry);
        this.regionViewFile = RegionViewSubfile.parse(new FileBuffer(regionViewFileData));
    }

    private void readTerrain() throws IOException {
        // Region view must be available for the following code to work
        if (this.regionViewFile == null) {
            this.readRegionView();
        }

        IndexEntry entry = this.getEntry(SC4ResourceKeys.TERRAIN_MAP);
        if (entry == null) {
            return;
        }

        byte[] terrainFileData = this.getBytesAtIndex(entry);
        this.terrainFile = TerrainSubfile.parse(new FileBuffer(terrainFileData), this.regionViewFile.getCitySizeX(), this.regionViewFile.getCitySizeY());
    }
}
