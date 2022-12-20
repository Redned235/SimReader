package me.redned.simreader.sc4.storage.exemplar.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.redned.simreader.storage.FileBuffer;
import me.redned.simreader.storage.model.PersistentResourceKey;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ExemplarSubfile {
    private final String fileIdentifier;
    private final Format format;
    private final int versionNumber;
    private final int type;
    private final int group;
    private final int instance;
    private final List<Property> properties;

    public PersistentResourceKey getCohortResourceKey() {
        return new PersistentResourceKey(this.type, this.group, this.instance);
    }

    public static <T extends ExemplarSubfile> T parse(ExemplarSubfileFactory<T> factory, FileBuffer buffer) {
        String string = buffer.readString(8);
        Format format = string.charAt(3) == 'B' ? Format.BINARY : Format.TEXT;
        int versionNumber = string.charAt(4);

        int cohortType = buffer.readUInt32();
        int cohortGroup = buffer.readUInt32();
        int cohortInstance = buffer.readUInt32();

        int propertyCount = buffer.readUInt32();
        List<Property> properties = new ArrayList<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            int id = buffer.readUInt32();
            short valueType = buffer.readUInt16();
            short keyType = buffer.readUInt16();

            Property property = new Property(id);
            if (keyType == 0x80) {
                buffer.readByte(); // unused flag

                int count = buffer.readUInt32();
                if (valueType == 0x0C) {
                    // String uses count to read string size
                    buffer.readDataType(property.values, valueType, count);
                } else {
                    for (int j = 0; j < count; j++) {
                        buffer.readDataType(property.values, valueType);
                    }
                }
            } else {
                buffer.readByte(); // count - should always be 0
                buffer.readDataType(property.values, valueType);
            }
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

    public enum Format {
        TEXT,
        BINARY
    }

    @RequiredArgsConstructor
    @Getter
    public static class Property {
        private final int id;
        private final List<Object> values = new ArrayList<>();
    }
}
