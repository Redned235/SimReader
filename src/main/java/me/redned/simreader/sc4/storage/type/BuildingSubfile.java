package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.storage.SC4File;
import me.redned.simreader.sc4.type.Building;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BuildingSubfile {
    private final List<Building> buildings;

    public static BuildingSubfile parse(SC4File sc4File, byte[] bytes, int size) {
        List<Building> buildings = new ArrayList<>();

        int offset = 0;
        while (size > 0) {
            int currentSize = Utils.readUInt32(offset, bytes);

            byte[] buildingBytes = new byte[currentSize];
            System.arraycopy(bytes, offset, buildingBytes, 0, currentSize);

            Building building = Building.parse(new FileBuffer(buildingBytes, sc4File, offset));
            buildings.add(building);

            offset += currentSize;
            size -= currentSize;
        }

        if (size != 0) {
            System.err.println("Did not read all buildings! Had " + size + " remaining bytes!");
        }

        return new BuildingSubfile(buildings);
    }
}
