package me.redned.simreader.sc4.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.PersistentResourceKey;
import me.redned.simreader.storage.model.SaveGameProperty;

import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Flora {
    private final int offset;
    private final int size;
    private final int crc;
    private final int memory;
    private final short majorVersion;
    private final short minorVersion;
    private final short zotWord;
    private final byte unknown1;
    private final byte appearanceFlag;
    private final int x74758926; // unknown
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
    private final int instanceIdOnAppearance;
    private final float coordinateX;
    private final float coordinateY;
    private final float coordinateZ;
    private final int currentGrowthCycleDate;
    private final int firstAppearanceDate;
    private final byte state;
    private final byte orientation;
    private final int objectId;

    public PersistentResourceKey getResourceKey() {
        return new PersistentResourceKey(this.typeId, this.groupId, this.instanceId);
    }

    public static Flora parse(FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        int saveGamePropertyCount;
        return new Flora(
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
                buffer.sc4ReadAspyrSafe(buffer::readUInt16, 4),
                saveGamePropertyCount = buffer.readUInt32(),
                SaveGameProperty.parseAll(buffer, saveGamePropertyCount),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readUInt32()
        );
    }
}
