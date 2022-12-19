package me.redned.simreader.sc4.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.storage.FileBuffer;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Lot {
    private final int offset;
    private final int size;
    private final int crc;
    private final int memory;
    private final short majorVersion;
    private final int instanceId;

    private final byte flagByte1;

    private final byte minTileX;
    private final byte minTileZ;
    private final byte maxTileX;
    private final byte maxTileZ;

    private final byte commuteTileX;
    private final byte commuteTileZ;

    private final float positionY;
    private final float slope1Y;
    private final float slope2Y;

    private final byte sizeX;
    private final byte sizeZ;

    private final byte orientation;

    private final byte flagByte2;
    private final byte flagByte3;

    private final LotZoneType zoneType;
    private final byte zoneWealth;

    private final int dateLotAppeared;
    private final int buildingInstanceId;

    private final byte unknown;

    public static Lot parse(FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        return new Lot(
                offset,
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt16(),
                buffer.readUInt16(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readSingle(),
                buffer.readSingle(),
                buffer.readSingle(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                LotZoneType.values()[buffer.readByte()],
                buffer.readByte(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readByte()
        );
    }
}
