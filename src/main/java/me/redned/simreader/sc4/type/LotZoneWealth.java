package me.redned.simreader.sc4.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum LotZoneWealth {
    NONE(0x00),
    $(0x01),
    $$(0x02),
    $$$(0x03);

    private final int id;

    private static final Map<Integer, LotZoneWealth> BY_ID = new HashMap<>();

    static {
        for (LotZoneWealth value : LotZoneWealth.values()) {
            BY_ID.put(value.id, value);
        }
    }

    public static LotZoneWealth byId(int id) {
        return BY_ID.get(id);
    }
}
