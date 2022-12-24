package me.redned.simreader.sc4.storage.exemplar.property.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;

public class UInt32Property {

    public static class Single extends ExemplarProperty.Single<Integer> {
    }

    public static class Multi extends ExemplarProperty.Multi<Integer> {
        public Multi(int size) {
            super(size);
        }
    }
}
