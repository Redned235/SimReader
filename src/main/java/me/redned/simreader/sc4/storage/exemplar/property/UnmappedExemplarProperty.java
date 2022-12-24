package me.redned.simreader.sc4.storage.exemplar.property;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class UnmappedExemplarProperty extends ExemplarProperty<Object, List<Object>> {
    protected final List<Object> values = new ArrayList<>();

    @Override
    public List<Object> getValue() {
        return this.values;
    }

    @Override
    public void readProperty(Object value) {
        this.values.add(value);
    }
}
