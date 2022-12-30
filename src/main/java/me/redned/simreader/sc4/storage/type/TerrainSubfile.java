package me.redned.simreader.sc4.storage.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;

@ToString
@Getter
@RequiredArgsConstructor
public class TerrainSubfile {
    private final short majorVersion;
    private final float[][] heightMap;

    public static TerrainSubfile parse(FileBuffer buffer, int citySizeX, int citySizeY) {
        short majorVersion = buffer.readUInt16();

        citySizeX += 1;
        citySizeY += 1;

        float[][] heightMap = new float[citySizeX][];
        for (int x = 0; x < citySizeX; x++) {
            heightMap[x] = new float[citySizeY];
            for (int y = 0; y < citySizeY; y++) {
                heightMap[x][y] = buffer.readFloat32();
            }
        }

        return new TerrainSubfile(majorVersion, heightMap);
    }
}
