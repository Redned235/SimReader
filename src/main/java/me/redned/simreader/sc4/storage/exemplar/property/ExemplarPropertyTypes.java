package me.redned.simreader.sc4.storage.exemplar.property;

import me.redned.simreader.sc4.storage.exemplar.property.type.BooleanProperty;
import me.redned.simreader.sc4.storage.exemplar.property.type.ResourceKeyProperty;
import me.redned.simreader.sc4.storage.exemplar.property.type.StringProperty;
import me.redned.simreader.sc4.storage.exemplar.property.type.UInt32Property;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * A list of property types used within exemplars.
 * <p>
 * Data retrieved from here: <a href="https://wiki.sc4devotion.com/index.php?title=Exemplar_properties_XML">...</a>
 */
public class ExemplarPropertyTypes {
    private static final Map<Integer, Type<?>> EXEMPLAR_PROPERTIES = new HashMap<>();

    // Common
    public static final Type<UInt32Property.Single> EXEMPLAR_TYPE = new Type<>(0x10, UInt32Property.Single.class, UInt32Property.Single::new);
    public static final Type<UInt32Property.Single> EXEMPLAR_INTERFACE = new Type<>(0x11, UInt32Property.Single.class, UInt32Property.Single::new);
    public static final Type<UInt32Property.Single> EXEMPLAR_CLASS_ID = new Type<>(0x12, UInt32Property.Single.class, UInt32Property.Single::new);
    public static final Type<StringProperty.Single> EXEMPLAR_NAME = new Type<>(0x20, StringProperty.Single.class, StringProperty.Single::new);
    public static final Type<UInt32Property.Single> EXEMPLAR_ID = new Type<>(0x21, UInt32Property.Single.class, UInt32Property.Single::new);
    public static final Type<UInt32Property.Single> PLUGIN_PACK_ID = new Type<>(0x6a871b82, UInt32Property.Single.class, UInt32Property.Single::new);
    public static final Type<ResourceKeyProperty> USER_VISIBLE_NAME_KEY = new Type<>(0x8a416a99, ResourceKeyProperty.class, ResourceKeyProperty::new);
    public static final Type<BooleanProperty.Single> HOVER_QUERY_ONLY = new Type<>(0xcaa9ab92, BooleanProperty.Single.class, BooleanProperty.Single::new);
    public static final Type<UInt32Property.Single> EXEMPLAR_CATEGORY = new Type<>(0x2c8f8746, UInt32Property.Single.class, UInt32Property.Single::new);

    // Resource Keys
    public static final Type<ResourceKeyProperty> RESOURCE_KEY_TYPE_0 = new Type<>(0x27812820, ResourceKeyProperty.class, ResourceKeyProperty::new);
    public static final Type<ResourceKeyProperty> RESOURCE_KEY_TYPE_1 = new Type<>(0x27812821, ResourceKeyProperty.class, ResourceKeyProperty::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_2 = new Type<>(0x27812822, 20, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_3 = new Type<>(0x27812823, 7, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_4 = new Type<>(0x27812824, -1, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<ResourceKeyProperty> RESOURCE_KEY_TYPE_5 = new Type<>(0x27812825, ResourceKeyProperty.class, ResourceKeyProperty::new);
    public static final Type<ResourceKeyProperty> RESOURCE_KEY_TYPE_1XM = new Type<>(0x27812921, ResourceKeyProperty.class, ResourceKeyProperty::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_2XM = new Type<>(0x27812922, 20, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_3XM = new Type<>(0x27812923, 7, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<UInt32Property.Multi> RESOURCE_KEY_TYPE_4XM = new Type<>(0x27812924, -1, UInt32Property.Multi.class, UInt32Property.Multi::new);
    public static final Type<ResourceKeyProperty> RESOURCE_KEY_TYPE_5XM = new Type<>(0x27812925, ResourceKeyProperty.class, ResourceKeyProperty::new);

    private static final Type<UnmappedExemplarProperty> UNMAPPED = new Type<>(-1, UnmappedExemplarProperty.class, UnmappedExemplarProperty::new);

    @SuppressWarnings("unchecked")
    public static <T, V> ExemplarProperty<T, V> read(int id) {
        return (ExemplarProperty<T, V>) EXEMPLAR_PROPERTIES.getOrDefault(id, UNMAPPED).supplier().get();
    }

    public record Type<T extends ExemplarProperty<?, ?>>(int id, Class<T> type, Supplier<T> supplier) {
        public Type(int id, int size, Class<T> type, IntFunction<T> function) {
            this(id, type, () -> function.apply(size));
        }

        public Type(int id, Class<T> type, Supplier<T> supplier) {
            this.id = id;
            this.type = type;
            this.supplier = supplier;

            EXEMPLAR_PROPERTIES.put(id, this);
        }
     }
}
