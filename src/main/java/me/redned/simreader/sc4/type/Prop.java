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
public class Prop {
    private final int offset;
    private final int size;
    private final int crc;
    private final int memory;
    private final short majorVersion;
    private final short minorVersion;
    private final short zotWord;
    private final byte unknown1;
    private final byte appearanceFlag;
    private final int xA823821E; // unknown
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
    private final float minCoordinateX;
    private final float minCoordinateY;
    private final float minCoordinateZ;
    private final float maxCoordinateX;
    private final float maxCoordinateY;
    private final float maxCoordinateZ;
    private final byte orientation;
    private final byte state;
    private final byte startTime;
    private final byte stopTime;
    private final byte count;

    private final int interval;
    private final int duration;
    private final int startDate;
    private final int endDate;

    private final byte appearanceChance;
    private final byte lotType;
    private final int objectId;
    private final byte conditionalAppearance;

    public PersistentResourceKey getResourceKey() {
        return new PersistentResourceKey(this.typeId, this.groupId, this.instanceId);
    }

    public static Prop parse(FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        int saveGamePropertyCount;
        byte count;
        return new Prop(
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
                buffer.readUInt32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                count = buffer.readByte(),
                count == 1 ? buffer.readInt32() : 0,
                count == 1 ? buffer.readInt32() : 0,
                count == 1 ? buffer.readInt32() : 0,
                count == 1 ? buffer.readInt32() : 0,
                buffer.readByte(),
                buffer.readByte(),
                buffer.readInt32(),
                buffer.readByte()
        );
    }
}
