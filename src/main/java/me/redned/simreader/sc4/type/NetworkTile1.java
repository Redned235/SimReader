package me.redned.simreader.sc4.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.SaveGameProperty;

import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkTile1 {
    private final int offset;
    private final int recordSize;
    private final int crc;
    private final int memory;
    private final short majorVersion;
    private final short minorVersion;
    private final short zotBytes;
    private final byte unknown1;
    private final byte appearanceFlag;
    private final int xC772BF98; // unknown;
    private final byte minTractX;
    private final byte minTractZ;
    private final byte maxTractX;
    private final byte maxTractZ;
    private final short tractSizeX;
    private final short tractSizeZ;
    private final int saveGamePropertyCount;
    private final List<SaveGameProperty> properties;
    private final int groupId;
    private final int typeId;
    private final int instanceId;
    private final byte unknown2;
    private final float coordinateX;
    private final float coordinateY;
    private final float coordinateZ;
    private final TextureDrawing texture1;
    private final TextureDrawing texture2;
    private final TextureDrawing texture3;
    private final TextureDrawing texture4;
    private final int textureId;
    private final byte orientation;
    private final NetworkType networkType;
    private final byte connectionWest;
    private final byte connectionNorth;
    private final byte connectionEast;
    private final byte connectionSouth;
    private final byte crossingFlag;
    private final float minCoordinateX;
    private final float maxCoordinateX;
    private final float minCoordinateY;
    private final float maxCoordinateY;
    private final float minCoordinateZ;
    private final float maxCoordinateZ;
    private final byte unknown3;
    private final byte roadConstructionFlag;
    private final byte unknown4;
    private final byte roadConstructionFlag2;

    public static NetworkTile1 parse(FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        int saveGamePropertyCount;
        return new NetworkTile1(
                offset,
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt16(),
                buffer.readUInt16(),
                buffer.readUInt16(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readUInt32(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readUInt16(),
                buffer.readUInt16(),
                saveGamePropertyCount = buffer.readUInt32(),
                SaveGameProperty.parseAll(buffer, saveGamePropertyCount),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readByte(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                TextureDrawing.parse(buffer),
                TextureDrawing.parse(buffer),
                TextureDrawing.parse(buffer),
                TextureDrawing.parse(buffer),
                buffer.readAndSkip(buffer::readUInt32, 5),
                buffer.readAndSkip(buffer::readByte, 3),
                NetworkType.byId(buffer.readByte()),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readAndRun(buffer::readByte, crossingFlag -> {
                    if (crossingFlag == 0x03) {
                        // Apparently there are hundreds of bytes that can be here and how to properly parse
                        // them is unknown. Just set the current cursor to the max minus the known byte count
                        // from the next position.
                        buffer.position(buffer.size() - 55);
                    }

                    buffer.skip(3);
                }),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte()
        );
    }

    @ToString
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TextureDrawing {
        private final float vertexX;
        private final float vertexY;
        private final float vertexZ;
        private final float u;
        private final float v;
        private final byte r;
        private final byte g;
        private final byte b;
        private final byte a;

        public static TextureDrawing parse(FileBuffer buffer) {
            return new TextureDrawing(
                    buffer.readFloat32(),
                    buffer.readFloat32(),
                    buffer.readFloat32(),
                    buffer.readFloat32(),
                    buffer.readFloat32(),
                    buffer.readByte(),
                    buffer.readByte(),
                    buffer.readByte(),
                    buffer.readByte()
            );
        }
    }
}
