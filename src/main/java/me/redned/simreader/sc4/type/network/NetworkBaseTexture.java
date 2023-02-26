package me.redned.simreader.sc4.type.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum NetworkBaseTexture {
    NONE(0x00000000),
    PAVEMENT_$(0x08100000),
    PAVEMENT_$$(0x08200000),
    PAVEMENT_$$$(0x08300000),
    DIRT(0x08400000);

    private final int id;

    private static final Map<Integer, NetworkBaseTexture> BY_ID = new HashMap<>();

    static {
        for (NetworkBaseTexture value : NetworkBaseTexture.values()) {
            BY_ID.put(value.id, value);
        }
    }

    public static NetworkBaseTexture byId(int id) {
        return BY_ID.get(id);
    }
}
