package me.redned.simreader.sc4.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.sc4.type.lot.LotZoneType;
import me.redned.simreader.sc4.type.lot.LotZoneWealth;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.SaveGameProperty;

import java.util.ArrayList;
import java.util.List;

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

    private final int minTileX;
    private final int minTileZ;
    private final int maxTileX;
    private final int maxTileZ;

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
    private final LotZoneWealth zoneWealth;

    private final int dateLotAppeared;
    private final int buildingInstanceId;

    private final byte unknown;

    private final int linkedIndustrialLot;
    private final int industrialSubfileTypeId;

    private final int linkedAnchorLot;
    private final int anchorLotTypeId;

    private final byte count; // ???

    private final byte rciCount;
    private final List<RCI> rcis;

    private final byte rciCount2;
    private final List<RCI> rcis2;
    // private final int demandSourceIndex;
    // private final short totalCapacity;

    private final float total$Capacity;
    private final float total$$Capacity;
    private final float total$$$Capacity;

    private final short unknown2;

    private final int saveGamePropertyCount;
    private final List<SaveGameProperty> properties;

    public static Lot parse(FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        int linkedIndustrialLot;
        int linkedAnchorLot;
        byte count;
        byte rciCount;
        byte rciCount2;
        int saveGamePropertyCount;
        return new Lot(
                offset,
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readUInt16(),
                buffer.readUInt32(),
                buffer.readByte(),
                buffer.readUByte(),
                buffer.readUByte(),
                buffer.readUByte(),
                buffer.readUByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                LotZoneType.byId(buffer.readByte()),
                LotZoneWealth.byId(buffer.readByte()),
                buffer.readUInt32(),
                buffer.readUInt32(),
                buffer.readByte(),
                linkedIndustrialLot = buffer.readInt32(),
                linkedIndustrialLot != 0x00000000 ? buffer.readInt32() : 0,
                linkedAnchorLot = buffer.readInt32(),
                linkedAnchorLot != 0x00000000 ? buffer.readInt32() : 0,
                count = buffer.readByte(),
                rciCount = (count == 0 ? 0 : buffer.readByte()),
                RCI.parseAll(buffer, rciCount, count),
                rciCount2 = buffer.readByte(),
                RCI.parseAll(buffer, rciCount2, count),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readFloat32(),
                buffer.readUInt16(),
                saveGamePropertyCount = buffer.readUInt32(),
                SaveGameProperty.parseAll(buffer, saveGamePropertyCount)
        );
    }

    @ToString
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RCI {
        private final int demandSourceIndex;
        private final short totalCapacity;

        public static RCI parse(FileBuffer buffer) {
            return new RCI(
                    buffer.readUInt32(),
                    buffer.readUInt16()
            );
        }

        public static List<RCI> parseAll(FileBuffer buffer, int rciCount, int count) {
            List<RCI> rcis = new ArrayList<>();
            for (int i = 0; i < rciCount; i++) {
                RCI rci = RCI.parse(buffer);
                rcis.add(rci);
            }

            return rcis;
        }
    }
}
