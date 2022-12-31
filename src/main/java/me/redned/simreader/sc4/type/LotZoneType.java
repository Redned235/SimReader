package me.redned.simreader.sc4.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum LotZoneType {
    UNKNOWN(0x00),
    RESIDENTIAL_LOW(0x01),
    RESIDENTIAL_MEDIUM(0x02),
    RESIDENTIAL_HIGH(0x03),
    COMMERCIAL_LOW(0x04),
    COMMERCIAL_MEDIUM(0x05),
    COMMERCIAL_HIGH(0x06),
    INDUSTRIAL_LOW(0x07),
    INDUSTRIAL_MEDIUM(0x08),
    INDUSTRIAL_HIGH(0x09),
    MILITARY(0x0A),
    AIRPORT(0x0B),
    SEAPORT(0x0C),
    SPACEPORT(0x0D),
    PLOPPED_BUILDING(0x0E),
    PLOPPED_BUILDING_2(0x0F);

    private final int id;

    private static final Map<Integer, LotZoneType> BY_ID = new HashMap<>();

    static {
        for (LotZoneType value : LotZoneType.values()) {
            BY_ID.put(value.id, value);
        }
    }

    public static LotZoneType byId(int id) {
        return BY_ID.get(id);
    }
}
