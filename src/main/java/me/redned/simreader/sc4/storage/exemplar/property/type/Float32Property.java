package me.redned.simreader.sc4.storage.exemplar.property.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;

public class Float32Property {

    public static class Single extends ExemplarProperty.Single<Float> {
    }

    public static class Multi extends ExemplarProperty.Multi<Float> {
        public Multi(int size) {
            super(size);
        }
    }
}
