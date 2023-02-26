package me.redned.simreader.sc4.type.network;

public enum NetworkWealthTexture {
    DEFAULT,
    GRASS_LOW_DENSITY_$,
    GRASS_LOW_DENSITY_$$,
    GRASS_LOW_DENSITY_$$$,
    MEDIUM_HIGH_DENSITY_$,
    MEDIUM_HIGH_DENSITY_$$,
    MEDIUM_HIGH_DENSITY_$$$,
    DIRT;

    private static final NetworkWealthTexture[] VALUES = values();

    public static NetworkWealthTexture byId(int id) {
        return VALUES[id];
    }
}
