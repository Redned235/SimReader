package me.redned.simreader.sc4.storage.exemplar.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.sc4.storage.exemplar.property.ExemplarProperty;
import me.redned.simreader.sc4.storage.exemplar.property.ExemplarPropertyTypes;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.PersistentResourceKey;
import me.redned.simreader.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ExemplarSubfile {
    private static final Map<Integer, Utils.ValueTypeReader> EXEMPLAR_DATA_READERS = Map.of(
            0x100, (data, buffer, len) -> data.add(buffer.readByte()),
            0x200, (data, buffer, len) -> data.add(buffer.readUInt16()),
            0x300, (data, buffer, len) -> data.add(buffer.readUInt32()),
            0x700, (data, buffer, len) -> data.add(buffer.readInt32()),
            0x800, (data, buffer, len) -> data.add(buffer.readInt64()),
            0x900, (data, buffer, len) -> data.add(buffer.readFloat32()),
            0xB00, (data, buffer, len) -> data.add(buffer.readBoolean()),
            0xC00, (data, buffer, len) -> data.add(buffer.readString(len))
    );

    private final String fileIdentifier;
    private final Format format;
    private final int versionNumber;
    private final int type;
    private final int group;
    private final int instance;
    private final Map<Integer, ExemplarProperty<?, ?>> properties;

    private CohortSubfile cohort;

    public PersistentResourceKey getCohortResourceKey() {
        return new PersistentResourceKey(this.type, this.group, this.instance);
    }

    public void setCohort(CohortSubfile cohort) {
        this.cohort = cohort;
    }

    public <T extends ExemplarProperty<?, ?>> T getProperty(ExemplarPropertyTypes.Type<T> type) {
        ExemplarProperty<?, ?> property = this.properties.get(type.id());
        if (property == null) {
            if (this.cohort != null) {
                property = this.cohort.getProperty(type);
            }

            if (property == null) {
                return null;
            }
        }

        return type.type().cast(property);
    }

    public static <T extends ExemplarSubfile> T parse(ExemplarSubfileFactory<T> factory, FileBuffer buffer) {
        String string = buffer.readString(8);
        Format format = string.charAt(3) == 'B' ? Format.BINARY : Format.TEXT;
        int versionNumber = string.charAt(4);

        int cohortType = buffer.readUInt32();
        int cohortGroup = buffer.readUInt32();
        int cohortInstance = buffer.readUInt32();

        int propertyCount = buffer.readUInt32();
        Map<Integer, ExemplarProperty<?, ?>> properties = new HashMap<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            int id = buffer.readUInt32();

            short valueType = buffer.readUInt16();
            short keyType = buffer.readUInt16();

            if (properties.containsKey(id)) {
                throw new IllegalArgumentException("Cannot insert property type multiple times in the same Exemplar!");
            }

            properties.put(id, readProperty(id, keyType, valueType, buffer));
        }

        return factory.create(
                string,
                format,
                versionNumber,
                cohortType,
                cohortGroup,
                cohortInstance,
                properties
        );
    }

    @SuppressWarnings("unchecked")
    private static <T> ExemplarProperty<T, ?> readProperty(int id, short keyType, short valueType, FileBuffer buffer) {
        ExemplarProperty<T, ?> property = ExemplarPropertyTypes.read(id);
        List<T> objects = new ArrayList<>();
        if (keyType == 0x80) {
            buffer.readByte(); // unused flag

            int count = buffer.readUInt32();
            if (valueType == 0xC00) {
                // String uses count to read string size
                buffer.readValueType(EXEMPLAR_DATA_READERS, (List<Object>) objects, valueType, count);
            } else {
                for (int j = 0; j < count; j++) {
                    buffer.readValueType(EXEMPLAR_DATA_READERS, (List<Object>) objects, valueType);
                }
            }
        } else {
            buffer.readByte(); // count - should always be 0
            buffer.readValueType(EXEMPLAR_DATA_READERS, (List<Object>) objects, valueType);
        }

        for (T object : objects) {
            property.readProperty(object);
        }

        property.onReadComplete();
        return property;
    }

    public enum Format {
        TEXT,
        BINARY
    }
}
