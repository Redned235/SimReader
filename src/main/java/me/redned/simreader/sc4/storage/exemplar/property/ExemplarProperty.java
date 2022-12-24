package me.redned.simreader.sc4.storage.exemplar.property;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class ExemplarProperty<T, V> {

    public abstract V getValue();

    public abstract void readProperty(T value);

    public void onReadComplete() {
    }

    @ToString
    public static class Single<T> extends ExemplarProperty<T, T> {
        protected T value;

        @Override
        public T getValue() {
            return this.value;
        }

        @Override
        public void readProperty(T value) {
            if (this.value != null) {
                throw new IllegalArgumentException("Cannot reassign already assigned property value!");
            }

            this.value = value;
        }
    }

    @ToString
    public static class Multi<T> extends ExemplarProperty<T, List<T>> {
        protected final int size;
        protected final List<T> values;

        public Multi(int size) {
            this.size = size;
            if (size == -1) {
                this.values = new ArrayList<>();
            } else {
                this.values = new ArrayList<>(size);
            }
        }

        @Override
        public List<T> getValue() {
            return this.values;
        }

        @Override
        public void readProperty(T value) {
            if (this.size > -1 && this.values.size() > this.size) {
                throw new IllegalArgumentException("Attempted to add property value " + value + ", but it exceeded the max size of " + this.size);
            }

            this.values.add(value);
        }
    }
}
