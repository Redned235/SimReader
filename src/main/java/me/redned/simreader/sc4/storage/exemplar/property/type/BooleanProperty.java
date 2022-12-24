package me.redned.simreader.sc4.storage.exemplar.property.type;

import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;

public class BooleanProperty {

    public static class Single extends ExemplarProperty.Single<Boolean> {
    }

    public static class Multi extends ExemplarProperty.Multi<Boolean> {
        public Multi(int size) {
            super(size);
        }
    }
}
