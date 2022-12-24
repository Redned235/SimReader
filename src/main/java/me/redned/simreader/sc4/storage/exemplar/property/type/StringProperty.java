package me.redned.simreader.sc4.storage.exemplar.property.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;

public class StringProperty {

    public static class Single extends ExemplarProperty.Single<String> {
    }

    public static class Multi extends ExemplarProperty.Multi<String> {
        public Multi(int size) {
            super(size);
        }
    }
}
