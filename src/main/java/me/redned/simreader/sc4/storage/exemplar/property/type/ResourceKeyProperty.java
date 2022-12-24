package me.redned.simreader.sc4.storage.exemplar.property.type;

import lombok.ToString;
import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;
import me.redned.simreader.storage.model.PersistentResourceKey;

@ToString
public class ResourceKeyProperty extends ExemplarProperty.Multi<Integer> {
    private PersistentResourceKey resourceKey;

    public ResourceKeyProperty() {
        super(3);
    }

    @Override
    public void onReadComplete() {
        if (this.values.size() != 3) {
            throw new IllegalArgumentException("Expected resource key property to contain 3 values, but contained " + this.values.size() + " values!");
        }

        this.resourceKey = new PersistentResourceKey(this.values.get(0), this.values.get(1), this.values.get(2));
    }

    public PersistentResourceKey getResourceKey() {
        return this.resourceKey;
    }
}
