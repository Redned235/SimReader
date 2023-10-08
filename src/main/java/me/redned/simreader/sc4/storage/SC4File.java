package me.redned.simreader.sc4.storage;

import lombok.Getter;
import lombok.Setter;
import me.redned.simreader.sc4.DecodingFormat;
import me.redned.simreader.sc4.SC4ResourceKeys;
import me.redned.simreader.sc4.SC4ResourceTypes;
import me.redned.simreader.sc4.storage.type.BuildingSubfile;
import me.redned.simreader.sc4.storage.type.FloraSubfile;
import me.redned.simreader.sc4.storage.type.LotSubfile;
import me.redned.simreader.sc4.storage.type.NetworkTile1Subfile;
import me.redned.simreader.sc4.storage.type.NetworkTile2Subfile;
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
    private NetworkTile1Subfile networkTile1File;
    private NetworkTile2Subfile networkTile2File;

    @Setter
    private DecodingFormat decodingFormat;

    public SC4File(Path path) throws IOException {
        this(path, DecodingFormat.AUTODETECT_FORMAT);
    }

    public SC4File(Path path, DecodingFormat decodingFormat) throws IOException {
        super(path);

        this.decodingFormat = decodingFormat;

        this.read();
    }

    @Override
    protected void read() throws IOException {
        super.read();

        this.readBuildings();
        this.readProps();
        this.readFlora();
        this.readRegionView();
        this.readTerrain();
        this.readNetworkTile1();
        this.readNetworkTile2();

        if (this.decodingFormat == DecodingFormat.AUTODETECT_FORMAT) {
            System.out.println("Format could not be detected automatically before parsing lots. Defaulting to Windows...");
            this.decodingFormat = DecodingFormat.WINDOWS;
        }

        this.readLots();
    }

    private void readLots() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.LOT_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] lotFileData = this.getBytesAtIndex(entry);
        this.lotFile = LotSubfile.parse(this, lotFileData, lotFileData.length);
    }

    private void readBuildings() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.BUILDING_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] buildingFileData = this.getBytesAtIndex(entry);
        this.buildingFile = BuildingSubfile.parse(this, buildingFileData, buildingFileData.length);
    }

    private void readProps() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.PROP_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] propFileData = this.getBytesAtIndex(entry);
        this.propFile = PropSubfile.parse(this, propFileData, propFileData.length);
    }

    private void readFlora() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.FLORA_SUBFILE);
        if (entry == null) {
            return;
        }

        byte[] floraFileData = this.getBytesAtIndex(entry);
        this.floraFile = FloraSubfile.parse(this, floraFileData, floraFileData.length);
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

    private void readNetworkTile1() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.NETWORK_SUBFILE_1);
        if (entry == null) {
            return;
        }

        byte[] networkTileData = this.getBytesAtIndex(entry);
        this.networkTile1File = NetworkTile1Subfile.parse(this, networkTileData, networkTileData.length);
    }

    private void readNetworkTile2() throws IOException {
        IndexEntry entry = this.getFirstEntryFromType(SC4ResourceTypes.NETWORK_SUBFILE_2);
        if (entry == null) {
            return;
        }

        byte[] networkTileData = this.getBytesAtIndex(entry);
        this.networkTile2File = NetworkTile2Subfile.parse(this, networkTileData, networkTileData.length);
    }
}
