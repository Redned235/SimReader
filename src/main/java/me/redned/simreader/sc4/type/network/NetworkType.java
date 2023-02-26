package me.redned.simreader.sc4.type.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum NetworkType {
    ROAD(0x00),
    RAIL(0x01),
    STREET(0x03),
    AVENUE(0x06),
    SUBWAY(0x07),
    ONE_WAY_ROAD(0x0A);

    private final int id;

    private static final Map<Integer, NetworkType> BY_ID = new HashMap<>();

    static {
        for (NetworkType value : NetworkType.values()) {
            BY_ID.put(value.id, value);
        }
    }

    public static NetworkType byId(int id) {
        return BY_ID.get(id);
    }
}
