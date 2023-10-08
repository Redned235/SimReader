package me.redned.simreader.sc4.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.redned.simreader.sc4.DecodingFormat;
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

    private final byte count;
    private final List<RCI> rcis;

    private final byte rciCount;
    private final List<RCI> rcis2;
    // private final int demandSourceIndex;
    // private final short totalCapacity;

    private final float total$Capacity;
    private final float total$$Capacity;
    private final float total$$$Capacity;

    private final short unknown2;

    private final int saveGamePropertyCount;
    private final List<SaveGameProperty> properties;

    public static Lot parse(DecodingFormat format, FileBuffer buffer) {
        int offset = buffer.cursor();
        buffer.resetCursor();

        int linkedIndustrialLot;
        int linkedAnchorLot;
        byte count;
        byte rciCount;
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
                buffer.readAndSkipIf(buffer::readByte, buf -> format == DecodingFormat.MACOS, 4),
                linkedIndustrialLot = buffer.readUInt32(),
                format == DecodingFormat.MACOS ? (linkedIndustrialLot != 0 ? 0 : buffer.readUInt32()) : linkedIndustrialLot == 0 ? 0 : buffer.readUInt32(),
                linkedAnchorLot = buffer.readUInt32(),
                format == DecodingFormat.MACOS ? buffer.reader(() -> {
                    if (linkedAnchorLot != 0) {
                        buffer.offset(8);

                        if (buffer.peek(buffer::readUInt32) != 0) {
                            buffer.offset(4);
                        }
                    }

                    return buffer.readUInt32();
                }) : linkedAnchorLot == 0 ? 0 : buffer.readUInt32(),
                count = buffer.readByte(),
                RCI.parseCapacities(buffer, count),
                rciCount = buffer.readByte(),
                RCI.parseTotalCapacities(buffer, rciCount),
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

        public static List<RCI> parseCapacities(FileBuffer buffer, int count) {
            List<RCI> rcis = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                byte typeCount = buffer.readByte();
                for (int j = 0; j < typeCount; j++) {
                    RCI rci = RCI.parse(buffer);
                    rcis.add(rci);
                }
            }

            return rcis;
        }

        public static List<RCI> parseTotalCapacities(FileBuffer buffer, int rciCount) {
            List<RCI> rcis = new ArrayList<>();
            for (int i = 0; i < rciCount; i++) {
                RCI rci = RCI.parse(buffer);
                rcis.add(rci);
            }

            return rcis;
        }
    }
}
